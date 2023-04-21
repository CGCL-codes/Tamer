package org.pachyderm.authoring;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Locale;
import org.pachyderm.foundation.PXBindingDescription;
import org.pachyderm.foundation.PXBindingValues;
import org.pachyderm.foundation.PXComponent;
import org.pachyderm.foundation.PXConstantValueAssociation;
import org.pachyderm.foundation.PXUtility;
import org.pachyderm.apollo.app.InspectPageInterface;
import org.pachyderm.apollo.app.MC;
import org.pachyderm.apollo.app.MCComponent;
import org.pachyderm.apollo.app.MCPage;
import org.pachyderm.apollo.app.MCSelectPage;
import org.pachyderm.apollo.core.UTType;
import org.pachyderm.apollo.data.CXManagedObject;
import org.pachyderm.apollo.app.EditPageInterface;
import com.webobjects.appserver.WOComponent;
import com.webobjects.appserver.WOContext;
import com.webobjects.directtoweb.NextPageDelegate;
import com.webobjects.eocontrol.EOOrQualifier;
import com.webobjects.eocontrol.EOQualifier;
import com.webobjects.foundation.NSArray;
import com.webobjects.foundation.NSDictionary;
import com.webobjects.foundation.NSKeyValueCoding;
import com.webobjects.foundation.NSMutableArray;
import com.webobjects.foundation.NSSelector;
import com.webobjects.foundation.NSSize;

/**
 * @author jarcher
 *
 */
public class InlineMediaAssetSelector extends InlineBindingEditor {

    /**
	 * 
	 */
    private static final long serialVersionUID = -7689077878296237437L;

    private NSSelector _setObjectFilterSel = new NSSelector("setObjectFilter", new Class[] { EOQualifier.class });

    @SuppressWarnings("unused")
    private static final NSDictionary PreviewImageContext = new NSDictionary(new Object[] { UTType.Image, new NSSize(59.0f, 67.0f) }, new String[] { "type", "size" });

    /**
	 * @param context
	 */
    public InlineMediaAssetSelector(final WOContext context) {
        super(context);
    }

    /**
	 *  Description of the Class
	 *
	 * @author     dnorman
	 * @created    July 19, 2005
	 */
    public class SelectBindingValueDelegate implements NextPageDelegate {

        private PXComponent _source;

        private String _key;

        private WOComponent _nextPage;

        /**
		 * @param source
		 * @param key
		 * @param page
		 */
        public SelectBindingValueDelegate(PXComponent source, String key, WOComponent page) {
            super();
            _source = source;
            _key = key;
            _nextPage = page;
        }

        public WOComponent nextPage(WOComponent sender) {
            if (sender instanceof MCSelectPage) {
                Object value = ((MCSelectPage) sender).selectedObject();
                PXBindingValues values = _source.bindingValues();
                values.willChange();
                PXConstantValueAssociation assoc = new PXConstantValueAssociation();
                assoc.setConstantValue(value);
                Locale locale = (Locale) session().objectForKey("EditScreenPage_locale");
                if (locale == null) {
                    locale = Locale.getDefault();
                }
                values.takeStoredLocalizedValueForKey(assoc, _key, locale);
            }
            return _nextPage;
        }
    }

    /**
	 *  Gets the imageType attribute of the InlineMediaAssetSelector object
	 *
	 * @return    The imageType value
	 */
    public boolean isImageType() {
        return _typeConformsTo(UTType.Image);
    }

    /**
	 *  Gets the audioType attribute of the InlineMediaAssetSelector object
	 *
	 * @return    The audioType value
	 */
    public boolean isAudioType() {
        return ((_typeConformsTo(UTType.Audio)) || (_typeConformsTo(UTType.MP3)) || (_typeConformsTo(UTType.MPEG4Audio)) || (_typeConformsTo(UTType.AppleProtectedMPEG4Audio)));
    }

    /**
	 *  Gets the videoType attribute of the InlineMediaAssetSelector object
	 *
	 * @return    The videoType value
	 */
    public boolean isVideoType() {
        return ((_typeConformsTo(UTType.Video)) || (_typeConformsTo(UTType.QuickTimeMovie)) || (_typeConformsTo(UTType.MPEG)) || (_typeConformsTo(UTType.MPEG4)) || (_typeConformsTo(UTType.AudiovisualContent)));
    }

    /**
	 *  Description of the Method
	 *
	 * @param  uti  Description of the Parameter
	 * @return      Description of the Return Value
	 */
    private boolean _typeConformsTo(String uti) {
        String type = contentType();
        return UTType.typeConformsTo(type, uti);
    }

    /**
	 * @return
	 */
    public WOComponent selectImage() {
        NSDictionary additionalBindings = new NSDictionary(new NSArray("screenEdit"), new NSArray("taskContext"));
        MCPage page = (MCPage) MC.mcfactory().pageForTaskTypeTarget("select", "pachyderm.resource", "web", context(), additionalBindings);
        if (_setObjectFilterSel.implementedByObject(page)) {
            try {
                PXBindingDescription description = bindingDescription();
                NSArray contentTypes = description.contentTypes();
                int i, count = contentTypes.count();
                NSMutableArray typeQuals = new NSMutableArray(count);
                for (i = 0; i < count; i++) {
                    typeQuals.addObject(new PXUTQualifier("ContentType", PXUTQualifier.ConformsTo, (String) contentTypes.objectAtIndex(i)));
                }
                EOQualifier filter = (typeQuals.count() > 0) ? new EOOrQualifier(typeQuals) : null;
                _setObjectFilterSel.invoke(page, filter);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        NextPageDelegate npd = new SelectBindingValueDelegate(component(), bindingKey(), context().page());
        page.setNextPageDelegate(npd);
        page.setObject(((MCComponent) context().page()).d2wContext().valueForKey("object"));
        return page;
    }

    /**
	 * @return
	 */
    public WOComponent editMedia() {
        NSDictionary additionalBindings = new NSDictionary(new NSArray("screenEdit"), new NSArray("taskContext"));
        EditPageInterface page = MC.mcfactory().editPageForTypeTarget("pachyderm.resource", "web", session());
        if (_setObjectFilterSel.implementedByObject(page)) {
            try {
                PXBindingDescription description = bindingDescription();
                NSArray contentTypes = description.contentTypes();
                int i, count = contentTypes.count();
                NSMutableArray typeQuals = new NSMutableArray(count);
                for (i = 0; i < count; i++) {
                    typeQuals.addObject(new PXUTQualifier("ContentType", PXUTQualifier.ConformsTo, (String) contentTypes.objectAtIndex(i)));
                }
                EOQualifier filter = (typeQuals.count() > 0) ? new EOOrQualifier(typeQuals) : null;
                _setObjectFilterSel.invoke(page, filter);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        NextPageDelegate npd = new SelectBindingValueDelegate(component(), bindingKey(), context().page());
        page.setNextPageDelegate(npd);
        page.setObject((CXManagedObject) evaluatedValue());
        return (WOComponent) page;
    }

    public WOComponent viewMedia() {
        NSDictionary additionalBindings = new NSDictionary(new NSArray("screenEdit"), new NSArray("taskContext"));
        InspectPageInterface page = MC.mcfactory().inspectPageForTypeTarget("pachyderm.resource", "web", session());
        if (_setObjectFilterSel.implementedByObject(page)) {
            try {
                PXBindingDescription description = bindingDescription();
                NSArray contentTypes = description.contentTypes();
                int i, count = contentTypes.count();
                NSMutableArray typeQuals = new NSMutableArray(count);
                for (i = 0; i < count; i++) {
                    typeQuals.addObject(new PXUTQualifier("ContentType", PXUTQualifier.ConformsTo, (String) contentTypes.objectAtIndex(i)));
                }
                EOQualifier filter = (typeQuals.count() > 0) ? new EOOrQualifier(typeQuals) : null;
                _setObjectFilterSel.invoke(page, filter);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        NextPageDelegate npd = new SelectBindingValueDelegate(component(), bindingKey(), context().page());
        page.setNextPageDelegate(npd);
        page.setObject((CXManagedObject) evaluatedValue());
        return (WOComponent) page;
    }

    /**
	 * @return
	 */
    public String title() {
        return ((CXManagedObject) evaluatedValue()).valueForAttribute("title").toString();
    }

    /**
	 * @return
	 */
    public String previewURL() {
        String previewURL = null;
        CXManagedObject object = (CXManagedObject) evaluatedValue();
        String assetRootURL = PXUtility.defaultValueForKey("AssetRootURL");
        if (isAudioType()) {
            previewURL = assetRootURL + "images/audioIcon.gif";
        } else if (isVideoType()) {
            if (object != null) {
                previewURL = assetRootURL + "images/icon-MediaSelected.gif";
            } else {
                previewURL = assetRootURL + "images/icon-NoMediaSelected.gif";
            }
        } else {
            previewURL = assetRootURL + "images/ImageSlug.jpg";
            if (object != null) {
                try {
                    if ((object.previewImage() != null) && (object.previewImage().url() != null)) {
                        return object.previewImage().url().toString();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return previewURL;
    }

    /**
	 * @return
	 */
    public String resourceURL() {
        CXManagedObject object = (CXManagedObject) evaluatedValue();
        if (object != null) {
            String location = (String) ((NSKeyValueCoding) object).valueForKey("location");
            String fileFormat = (String) ((NSKeyValueCoding) object).valueForKey("format");
            if ((fileFormat == null || fileFormat.equals("application/octet-stream")) && location != null) {
                String fileExtension = "";
                if (location != null && location.length() > 0) {
                    int lastDot = location.lastIndexOf(".");
                    if (lastDot >= 0) {
                        fileExtension = location.substring(lastDot + 1, location.length()).toLowerCase();
                    }
                }
                if (fileExtension.equals("flv")) {
                    fileFormat = "flv-application/octet-stream";
                }
            }
            if (fileFormat != null && fileFormat.equals("flv-application/octet-stream")) {
                String previewPath = "";
                try {
                    previewPath = URLEncoder.encode(location, "UTF-8");
                } catch (UnsupportedEncodingException e) {
                }
                String flv_preview_path = PXUtility.defaultValueForKey("FLVPreviewHTMLURL");
                if (flv_preview_path != null && flv_preview_path != "") {
                    location = flv_preview_path + "?previewPath=" + previewPath;
                }
            }
            return location;
        }
        return null;
    }

    /**
	 *  Gets the valueNull attribute of the InlineMediaAssetSelector object
	 *
	 * @return    The valueNull value
	 */
    public boolean isValueNull() {
        return (evaluatedValue() == null);
    }

    /**
	 * @return
	 */
    public boolean mayUserEditResource() {
        boolean answer = false;
        if (!isValueNull()) {
            String resourceId = ((CXManagedObject) evaluatedValue()).valueForAttribute("rightsHolder").toString();
            if ((resourceId != null) && (resourceId != "0") && (((Session) session()).userIsAdmin())) {
                answer = true;
            } else if ((resourceId.equals(((Session) session()).person().uniqueId()))) {
                answer = true;
            }
        }
        return answer;
    }

    /**
	 * @return
	 */
    public WOComponent removeValue() {
        setEvaluatedValue(null);
        return context().page();
    }
}
