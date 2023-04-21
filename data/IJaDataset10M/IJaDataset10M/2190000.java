package com.dotmarketing.services;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import org.apache.velocity.runtime.resource.ResourceManager;
import com.dotmarketing.beans.Identifier;
import com.dotmarketing.business.APILocator;
import com.dotmarketing.business.CacheLocator;
import com.dotmarketing.cache.FieldsCache;
import com.dotmarketing.cache.IdentifierCache;
import com.dotmarketing.cms.factories.PublicUserFactory;
import com.dotmarketing.exception.DotDataException;
import com.dotmarketing.exception.DotSecurityException;
import com.dotmarketing.factories.IdentifierFactory;
import com.dotmarketing.factories.InodeFactory;
import com.dotmarketing.portlets.categories.business.CategoryAPI;
import com.dotmarketing.portlets.categories.model.Category;
import com.dotmarketing.portlets.contentlet.business.ContentletAPI;
import com.dotmarketing.portlets.contentlet.business.DotContentletStateException;
import com.dotmarketing.portlets.contentlet.model.Contentlet;
import com.dotmarketing.portlets.files.model.File;
import com.dotmarketing.portlets.htmlpages.model.HTMLPage;
import com.dotmarketing.portlets.structure.business.FieldAPI;
import com.dotmarketing.portlets.structure.model.Field;
import com.dotmarketing.portlets.structure.model.Structure;
import com.dotmarketing.util.Config;
import com.dotmarketing.util.Logger;
import com.dotmarketing.util.UtilMethods;
import com.dotmarketing.velocity.DotResourceCache;
import com.liferay.portal.model.User;

/**
 * @author will
 */
public class ContentletMapServices {

    private static CategoryAPI categoryAPI = APILocator.getCategoryAPI();

    public static CategoryAPI getCategoryAPI() {
        return categoryAPI;
    }

    public static void setCategoryAPI(CategoryAPI categoryAPI) {
        ContentletMapServices.categoryAPI = categoryAPI;
    }

    public static void invalidate(Contentlet contentlet) throws DotDataException, DotSecurityException {
        invalidate(contentlet, true);
        invalidate(contentlet, false);
    }

    public static void invalidate(Contentlet content, boolean EDIT_MODE) throws DotDataException, DotSecurityException {
        removeContentletMapFile(content, EDIT_MODE);
    }

    public static InputStream buildVelocity(Contentlet content, boolean EDIT_MODE) throws DotDataException, DotSecurityException, DotContentletStateException {
        InputStream result;
        ContentletAPI conAPI = APILocator.getContentletAPI();
        User systemUser = PublicUserFactory.getSystemUser();
        if (content.getInode() == 0 || content.getIdentifier() == 0) {
            throw new DotContentletStateException("The contentlet inode and identifier must be set");
        }
        StringBuilder sb = new StringBuilder();
        String conTitle = conAPI.getName(content, PublicUserFactory.getSystemUser(), true);
        sb.append("#set( $content = ${contents.getEmptyMap()})\n");
        sb.append("$!content.put(\"permission\", $EDIT_CONTENT_PERMISSION" + content.getIdentifier() + " )\n");
        sb.append("$!content.put(\"inode\", " + content.getInode() + "  )\n");
        sb.append("$!content.put(\"identifier\", " + content.getIdentifier() + "  )\n");
        sb.append("$!content.put(\"structureInode\", " + content.getStructureInode() + "  )\n");
        sb.append("$!content.put(\"contentTitle\", \"" + UtilMethods.espaceForVelocity(conTitle) + "\" )\n");
        sb.append("$!content.put(\"detailPageURI\", \"" + getDetailPageURI(content) + "\"  )\n");
        Structure structure = content.getStructure();
        String modDateStr = UtilMethods.dateToHTMLDate((Date) content.getModDate(), "yyyy-MM-dd H:mm:ss");
        sb.append("#set($_dummy = $!content.put(\"contentLastModDate\", $date.toDate(\"yyyy-MM-dd H:mm:ss\", \"" + modDateStr + "\")))\n");
        sb.append("#set($_dummy = $!content.put(\"contentLastModUserId\", \"" + content.getModUser() + "\"))\n");
        if (content.getOwner() != null) sb.append("#set($_dummy = $!content.put(\"contentOwnerId\", \"" + content.getOwner() + "\"))\n");
        List<Field> fields = FieldsCache.getFieldsByStructureInode(content.getStructureInode());
        Iterator<Field> fieldsIt = fields.iterator();
        String widgetCode = "";
        while (fieldsIt.hasNext()) {
            Field field = (Field) fieldsIt.next();
            sb.append("\n\n##Set Field " + field.getFieldName() + " properties\n");
            String contField = field.getFieldContentlet();
            String contFieldValue = null;
            Object contFieldValueObject = null;
            FieldAPI fdAPI = APILocator.getFieldAPI();
            String folderPath = (!EDIT_MODE) ? "live" + java.io.File.separator : "working" + java.io.File.separator;
            String velPath = (!EDIT_MODE) ? "live/" : "working/";
            if (fdAPI.isElementConstant(field)) {
                if (field.getVelocityVarName().equals("widgetCode")) {
                    widgetCode = "#set($_dummy = $!content.put(\"" + field.getVelocityVarName() + "\", $velutil.mergeTemplate(\"" + velPath + content.getInode() + "_" + field.getInode() + "." + Config.getStringProperty("VELOCITY_FIELD_EXTENSION") + "\")))\n";
                    continue;
                } else {
                    sb.append("#set($_dummy = $!content.put(\"" + field.getVelocityVarName() + "\", $velutil.mergeTemplate(\"" + velPath + content.getInode() + "_" + field.getInode() + "." + Config.getStringProperty("VELOCITY_FIELD_EXTENSION") + "\")))\n");
                    continue;
                }
            }
            if (UtilMethods.isSet(contField)) {
                try {
                    contFieldValueObject = conAPI.getFieldValue(content, field);
                    contFieldValue = contFieldValueObject == null ? "" : contFieldValueObject.toString();
                } catch (Exception e) {
                    Logger.error(ContentletMapServices.class, "writeContentletToFile: " + e.getMessage());
                }
                if (!field.getFieldType().equals(Field.FieldType.DATE_TIME.toString()) && !field.getFieldType().equals(Field.FieldType.DATE.toString()) && !field.getFieldType().equals(Field.FieldType.TIME.toString())) {
                    if (fdAPI.isNumeric(field)) {
                        sb.append("#set($_dummy = $!content.put(\"" + field.getVelocityVarName() + "\", " + contFieldValue + "))\n");
                    } else {
                        sb.append("#set($_dummy = $!content.put(\"" + field.getVelocityVarName() + "\", $velutil.mergeTemplate(\"" + velPath + content.getInode() + "_" + field.getInode() + "." + Config.getStringProperty("VELOCITY_FIELD_EXTENSION") + "\")))\n");
                    }
                }
            }
            if (field.getFieldType().equals(Field.FieldType.TEXT.toString()) || field.getFieldType().equals(Field.FieldType.TEXT_AREA.toString()) || field.getFieldType().equals(Field.FieldType.WYSIWYG.toString())) {
            } else if (field.getFieldType().equals(Field.FieldType.IMAGE.toString())) {
                File image = new File();
                Identifier id = new Identifier();
                long identifierValue = content.getLongProperty(field.getVelocityVarName());
                if (identifierValue != 0) {
                    id.setInode(identifierValue);
                    if (EDIT_MODE) image = (File) IdentifierFactory.getWorkingChildOfClass(id, File.class); else image = (File) IdentifierFactory.getLiveChildOfClass(id, File.class);
                }
                if (identifierValue != 0 && image.getInode() == 0) {
                    image = (File) InodeFactory.getInode(identifierValue, File.class);
                    if (image.getInode() > 0) {
                        id = IdentifierCache.getIdentifierFromIdentifierCache(image.getIdentifier());
                        if (EDIT_MODE) image = (File) IdentifierFactory.getWorkingChildOfClass(id, File.class); else image = (File) IdentifierFactory.getLiveChildOfClass(id, File.class);
                    }
                }
                if (identifierValue == 0 || image.getInode() == 0) {
                    image = new File();
                }
                sb.append("#set($_dummy = $!content.put(\"" + field.getVelocityVarName() + "ImageInode\", " + image.getInode() + " ))\n");
                sb.append("#set($_dummy = $!content.put(\"" + field.getVelocityVarName() + "ImageIdentifier\", " + image.getIdentifier() + " ))\n");
                sb.append("#set($_dummy = $!content.put(\"" + field.getVelocityVarName() + "ImageWidth\", " + image.getWidth() + " ))\n");
                sb.append("#set($_dummy = $!content.put(\"" + field.getVelocityVarName() + "ImageHeight\", " + image.getHeight() + " ))\n");
                sb.append("#set($_dummy = $!content.put(\"" + field.getVelocityVarName() + "ImageExtension\", \"" + UtilMethods.espaceForVelocity(image.getExtension()) + "\" ))\n");
                String imageURI = image.getIdentifier() > 0 ? UtilMethods.espaceForVelocity("/dotAsset/" + image.getIdentifier() + "." + image.getExtension()) : "";
                sb.append("#set($_dummy = $!content.put(\"" + field.getVelocityVarName() + "ImageURI\", \"" + imageURI + "\" ))\n");
                sb.append("#set($_dummy = $!content.put(\"" + field.getVelocityVarName() + "ImageTitle\", \"" + UtilMethods.espaceForVelocity(image.getTitle()) + "\" ))\n");
                sb.append("#set($_dummy = $!content.put(\"" + field.getVelocityVarName() + "ImageFriendlyName\", \"" + UtilMethods.espaceForVelocity(image.getFriendlyName()) + "\" ))\n");
                sb.append("#set($_dummy = $!content.put(\"" + field.getVelocityVarName() + "ImagePath\", \"" + UtilMethods.espaceForVelocity(image.getPath()) + "\" ))\n");
                sb.append("#set($_dummy = $!content.put(\"" + field.getVelocityVarName() + "ImageName\", \"" + UtilMethods.espaceForVelocity(image.getNameOnly()) + "\" ))\n");
            } else if (field.getFieldType().equals(Field.FieldType.FILE.toString())) {
                File file = new File();
                Identifier id = new Identifier();
                long identifierValue = content.getLongProperty(field.getVelocityVarName());
                if (identifierValue != 0) {
                    id.setInode(identifierValue);
                    if (EDIT_MODE) file = (File) IdentifierFactory.getWorkingChildOfClass(id, File.class); else file = (File) IdentifierFactory.getLiveChildOfClass(id, File.class);
                }
                if (identifierValue != 0 && file.getInode() == 0) {
                    file = (File) InodeFactory.getInode(identifierValue, File.class);
                    if (file.getInode() > 0) {
                        id = IdentifierCache.getIdentifierFromIdentifierCache(file.getIdentifier());
                        if (EDIT_MODE) file = (File) IdentifierFactory.getWorkingChildOfClass(id, File.class); else file = (File) IdentifierFactory.getLiveChildOfClass(id, File.class);
                    }
                }
                if (identifierValue == 0 || file.getInode() == 0) {
                    file = new File();
                }
                sb.append("#set($_dummy = $!content.put(\"" + field.getVelocityVarName() + "FileInode\", " + file.getInode() + " ))\n");
                sb.append("#set($_dummy = $!content.put(\"" + field.getVelocityVarName() + "FileIdentifier\", " + file.getIdentifier() + " ))\n");
                sb.append("#set($_dummy = $!content.put(\"" + field.getVelocityVarName() + "FileFriendlyName\", \"" + UtilMethods.espaceForVelocity(file.getFriendlyName()) + "\" ))\n");
                sb.append("#set($_dummy = $!content.put(\"" + field.getVelocityVarName() + "FileExtension\", \"" + UtilMethods.espaceForVelocity(file.getExtension()) + "\" ))\n");
                String fileURI = file.getIdentifier() > 0 ? UtilMethods.espaceForVelocity("/dotAsset/" + file.getIdentifier() + "." + file.getExtension()) : "";
                sb.append("#set($_dummy = $!content.put(\"" + field.getVelocityVarName() + "FileURI\", \"" + fileURI + "\" ))\n");
                sb.append("#set($_dummy = $!content.put(\"" + field.getVelocityVarName() + "FileTitle\", \"" + UtilMethods.espaceForVelocity(file.getTitle()) + "\" ))\n");
                sb.append("#set($_dummy = $!content.put(\"" + field.getVelocityVarName() + "FilePath\", \"" + UtilMethods.espaceForVelocity(file.getPath()) + "\" ))\n");
                sb.append("#set($_dummy = $!content.put(\"" + field.getVelocityVarName() + "FileName\", \"" + UtilMethods.espaceForVelocity(file.getNameOnly()) + "\" ))\n");
            } else if (field.getFieldType().equals(Field.FieldType.SELECT.toString())) {
                sb.append("#set($_dummy = $!content.put(\"" + field.getVelocityVarName() + "SelectLabelsValues\", \"" + field.getValues().replaceAll("\\r\\n", " ").replaceAll("\\n", " ") + "\"))\n");
            } else if (field.getFieldType().equals(Field.FieldType.RADIO.toString())) {
                sb.append("#set($_dummy = $!content.put(\"" + field.getVelocityVarName() + "RadioLabelsValues\", \"" + field.getValues().replaceAll("\\r\\n", " ").replaceAll("\\n", " ") + "\"))\n");
            } else if (field.getFieldType().equals(Field.FieldType.CHECKBOX.toString())) {
                sb.append("#set($_dummy = $!content.put(\"" + field.getVelocityVarName() + "CheckboxLabelsValues\", \"" + field.getValues().replaceAll("\\r\\n", " ").replaceAll("\\n", " ") + "\"))\n");
            } else if (field.getFieldType().equals(Field.FieldType.DATE.toString())) {
                String shortFormat = "";
                String dbFormat = "";
                if (contFieldValueObject != null && contFieldValueObject instanceof Date) {
                    shortFormat = UtilMethods.dateToHTMLDate((Date) contFieldValueObject, "MM/dd/yyyy");
                    dbFormat = UtilMethods.dateToHTMLDate((Date) contFieldValueObject, "yyyy-MM-dd");
                }
                sb.append("#set($_dummy = $!content.put(\"" + field.getVelocityVarName() + "\", $date.toDate(\"yyyy-MM-dd\", \"" + dbFormat + "\")))\n");
                sb.append("#set($_dummy = $!content.put(\"" + field.getVelocityVarName() + "ShortFormat\", \"" + shortFormat + "\"))\n");
                sb.append("#set($_dummy = $!content.put(\"" + field.getVelocityVarName() + "DBFormat\", \"" + dbFormat + "\"))\n");
            } else if (field.getFieldType().equals(Field.FieldType.TIME.toString())) {
                String shortFormat = "";
                if (contFieldValueObject != null && contFieldValueObject instanceof Date) {
                    shortFormat = UtilMethods.dateToHTMLDate((Date) contFieldValueObject, "H:mm:ss");
                }
                sb.append("#set($_dummy = $!content.put(\"" + field.getVelocityVarName() + "\", $date.toDate(\"H:mm:ss\", \"" + shortFormat + "\")))\n");
                sb.append("#set($_dummy = $!content.put(\"" + field.getVelocityVarName() + "ShortFormat\", \"" + shortFormat + "\"))\n");
            } else if (field.getFieldType().equals(Field.FieldType.DATE_TIME.toString())) {
                String shortFormat = "";
                String longFormat = "";
                String dbFormat = "";
                if (contFieldValueObject != null && contFieldValueObject instanceof Date) {
                    shortFormat = UtilMethods.dateToHTMLDate((Date) contFieldValueObject, "MM/dd/yyyy");
                    longFormat = UtilMethods.dateToHTMLDate((Date) contFieldValueObject, "MM/dd/yyyy H:mm:ss");
                    dbFormat = UtilMethods.dateToHTMLDate((Date) contFieldValueObject, "yyyy-MM-dd H:mm:ss");
                }
                sb.append("#set($_dummy = $!content.put(\"" + field.getVelocityVarName() + "\", $date.toDate(\"yyyy-MM-dd H:mm:ss\", \"" + dbFormat + "\")))\n");
                sb.append("#set($_dummy = $!content.put(\"" + field.getVelocityVarName() + "ShortFormat\", \"" + shortFormat + "\"))\n");
                sb.append("#set($_dummy = $!content.put(\"" + field.getVelocityVarName() + "DBFormat\", \"" + dbFormat + "\"))\n");
                sb.append("#set($_dummy = $!content.put(\"" + field.getVelocityVarName() + "LongFormat\", \"" + longFormat + "\"))\n");
            } else if (field.getFieldType().equals(Field.FieldType.BUTTON.toString())) {
                sb.append("#set($_dummy = $!content.put(\"" + field.getVelocityVarName() + "ButtonValue\", \"" + (field.getFieldName() == null ? "" : field.getFieldName()) + "\"))\n");
                sb.append("#set($_dummy = $!content.put(\"" + field.getVelocityVarName() + "ButtonCode\", \"" + (field.getValues() == null ? "" : field.getValues()) + "\"))\n");
            } else if (field.getFieldType().equals(Field.FieldType.CATEGORY.toString())) {
                Category category = categoryAPI.find(field.getValues(), systemUser, false);
                List<Category> selectedCategories = categoryAPI.getParents(content, systemUser, false);
                String catNames = "";
                String catKeys = "";
                String catInodes = "";
                Set<Category> categoryList = new HashSet<Category>();
                List<Category> categoryTree = categoryAPI.getAllChildren(category, systemUser, false);
                if (selectedCategories.size() > 0 && categoryTree != null) {
                    for (int k = 0; k < categoryTree.size(); k++) {
                        Category cat = (Category) categoryTree.get(k);
                        for (Category categ : selectedCategories) {
                            if (categ.getInode() == cat.getInode()) {
                                categoryList.add(cat);
                            }
                        }
                    }
                }
                if (categoryList.size() > 0) {
                    Iterator<Category> it = categoryList.iterator();
                    while (it.hasNext()) {
                        Category cat = (Category) it.next();
                        catInodes += cat.getInode();
                        catNames += "\"" + cat.getCategoryName() + "\"";
                        catKeys += "\"" + cat.getKey() + "\"";
                        if (it.hasNext()) {
                            catInodes += ",";
                            catNames += ",";
                            catKeys += ",";
                        }
                    }
                }
                sb.append("#set($_dummy = $!content.put(\"" + field.getVelocityVarName() + "Categories\", [" + catInodes + "] ))\n");
                sb.append("#set($_dummy = $!content.put(\"" + field.getVelocityVarName() + "CategoriesNames\", [" + catNames + "] ))\n");
                sb.append("#set($_dummy = $!content.put(\"" + field.getVelocityVarName() + "CategoriesKeys\", [" + catKeys + "] ))\n");
            }
        }
        sb.append(widgetCode);
        String categories = "";
        String categoryNames = "";
        String categoryKeys = "";
        Set<Category> categoryList = new HashSet<Category>(categoryAPI.getParents(content, systemUser, false));
        if (categoryList != null) {
            Iterator<Category> it = categoryList.iterator();
            while (it.hasNext()) {
                Category category = it.next();
                categories += category.getInode();
                categoryNames += "\"" + category.getCategoryName() + "\"";
                categoryKeys += "\"" + category.getKey() + "\"";
                if (it.hasNext()) {
                    categories += ",";
                    categoryNames += ",";
                    categoryKeys += ",";
                }
            }
        }
        sb.append("#set($_dummy = $!content.put(\"ContentletCategories\", [" + categories + "] ))\n");
        sb.append("#set($_dummy = $!content.put(\"contentletCategories\", [" + categories + "] ))\n");
        sb.append("#set($_dummy = $!content.put(\"ContentletCategoriesNames\", [" + categoryNames + "] ))\n");
        sb.append("#set($_dummy = $!content.put(\"contentletCategoriesNames\", [" + categoryNames + "] ))\n");
        sb.append("#set($_dummy = $!content.put(\"ContentletCategoriesKeys\", [" + categoryKeys + "] ))\n");
        sb.append("#set($_dummy = $!content.put(\"contentletCategoriesKeys\", [" + categoryKeys + "] ))\n");
        boolean isWidget = false;
        if (structure.getStructureType() == Structure.STRUCTURE_TYPE_WIDGET) {
            isWidget = true;
            sb.append("#set($_dummy = $!content.put(\"isWidget\", \"" + true + "\"  ))\n");
        } else {
            sb.append("#set($_dummy = $!content.put(\"isWidget\", \"" + false + "\"  ))\n");
        }
        if (Config.getBooleanProperty("SHOW_VELOCITYFILES", false)) {
            try {
                String velocityRootPath = Config.getStringProperty("VELOCITY_ROOT");
                if (velocityRootPath.startsWith("/WEB-INF")) {
                    velocityRootPath = Config.CONTEXT.getRealPath(velocityRootPath);
                }
                velocityRootPath += java.io.File.separator;
                String veloExt = Config.getStringProperty("VELOCITY_CONTENT_MAP_EXTENSION");
                String baseFilename = String.format("%d_%d.%s", content.getIdentifier(), content.getLanguageId(), veloExt);
                String filePath = "working" + java.io.File.separator + baseFilename;
                saveToDisk(velocityRootPath, filePath, sb.toString());
                if (!EDIT_MODE) {
                    filePath = "live" + java.io.File.separator + baseFilename;
                    saveToDisk(velocityRootPath, filePath, sb.toString());
                }
            } catch (Exception e) {
                Logger.error(ContentletMapServices.class, e.toString(), e);
            }
        }
        try {
            result = new ByteArrayInputStream(sb.toString().getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e1) {
            result = new ByteArrayInputStream(sb.toString().getBytes());
            Logger.error(ContainerServices.class, e1.getMessage(), e1);
        }
        return result;
    }

    public static void unpublishContentletMapFile(Contentlet asset) throws DotDataException {
        Identifier identifier = IdentifierCache.getIdentifierFromIdentifierCache(asset);
        removeContentletMapFile(asset, identifier, false);
    }

    /**
	 * Will remove all contentlet map files within a structure for both live and working. Uses the system user.
	 * @param contentlets
	 * @throws DotSecurityException 
	 * @throws DotDataException 
	 */
    public static void removeContentletMapFile(Structure structure) throws DotDataException, DotSecurityException {
        ContentletAPI conAPI = APILocator.getContentletAPI();
        int limit = 500;
        int offset = 0;
        List<Contentlet> contentlets = conAPI.findByStructure(structure, PublicUserFactory.getSystemUser(), false, limit, offset);
        int size = contentlets.size();
        while (size > 0) {
            for (Contentlet contentlet : contentlets) {
                removeContentletMapFile(contentlet);
            }
            offset += limit;
            contentlets = conAPI.findByStructure(structure, PublicUserFactory.getSystemUser(), false, limit, offset);
            size = contentlets.size();
        }
    }

    /**
	 * Will remove all contentlet map files for both live and working
	 * @param contentlets
	 * @throws DotDataException 
	 */
    public static void removeContentletMapFile(Contentlet contentlet) throws DotDataException {
        removeContentletMapFile(contentlet, true);
        removeContentletMapFile(contentlet, false);
    }

    /**
	 * Will remove all contentlet map files for both live and working
	 * @param contentlets
	 */
    public static void removeContentletMapFile(List<Contentlet> contentlets) throws DotDataException {
        for (Contentlet contentlet : contentlets) {
            removeContentletMapFile(contentlet);
        }
    }

    public static void removeContentletMapFile(Contentlet asset, boolean EDIT_MODE) throws DotDataException {
        Identifier identifier = IdentifierCache.getIdentifierFromIdentifierCache(asset);
        removeContentletMapFile(asset, identifier, EDIT_MODE);
    }

    public static void removeContentletMapFile(Contentlet asset, Identifier identifier, boolean EDIT_MODE) {
        String folderPath = (!EDIT_MODE) ? "live/" : "working/";
        String velocityRoot = Config.CONTEXT.getRealPath("/WEB-INF/velocity/") + folderPath;
        String filePath = folderPath + identifier.getInode() + "_" + asset.getLanguageId() + "." + Config.getStringProperty("VELOCITY_CONTENT_MAP_EXTENSION");
        java.io.File f = new java.io.File(velocityRoot + filePath);
        f.delete();
        DotResourceCache vc = CacheLocator.getVeloctyResourceCache();
        vc.remove(ResourceManager.RESOURCE_TEMPLATE + filePath);
        List<Field> fields = FieldsCache.getFieldsByStructureInode(asset.getStructureInode());
        for (Field field : fields) {
            try {
                FieldServices.invalidate(field.getInode(), asset.getInode(), EDIT_MODE);
            } catch (DotDataException e) {
                Logger.error(ContentletServices.class, e.getMessage(), e);
            } catch (DotSecurityException e) {
                Logger.error(ContentletServices.class, e.getMessage(), e);
            }
        }
    }

    /**
	 * Returns the details page URI for a given <tt>contentlet</tt>. (Used by
	 * #detailPageLink macro)
	 * 
	 * @param the
	 *            given <tt>contentlet</tt>
	 * @return the details page URI
	 * 
	 * @author Dimitris Zavaliadis
	 * @version 1.0
	 */
    private static String getDetailPageURI(Contentlet contentlet) {
        String detailPageURI = null;
        Structure structure = contentlet.getStructure();
        Long detailPageId = structure.getPagedetail();
        try {
            Identifier pageIdentifier = IdentifierCache.getIdentifierFromIdentifierCache(detailPageId);
            if (pageIdentifier.getInode() == 0) {
                pageIdentifier = IdentifierCache.getIdentifierFromIdentifierCache((HTMLPage) InodeFactory.getInode(detailPageId, HTMLPage.class));
            }
            detailPageURI = pageIdentifier.getURI();
        } catch (Exception e) {
            Logger.error(ContentletMapServices.class, e.getMessage());
        }
        return detailPageURI;
    }

    private static void saveToDisk(String folderPath, String filePath, String data) throws IOException {
        java.io.BufferedOutputStream tmpOut = new java.io.BufferedOutputStream(new java.io.FileOutputStream(new java.io.File(folderPath + filePath)));
        OutputStreamWriter out = new OutputStreamWriter(tmpOut, UtilMethods.getCharsetConfiguration());
        out.write(data);
        out.flush();
        out.close();
        tmpOut.close();
        DotResourceCache vc = CacheLocator.getVeloctyResourceCache();
        vc.remove(ResourceManager.RESOURCE_TEMPLATE + filePath);
    }
}
