package photospace.web.spring;

import java.awt.image.*;
import java.io.*;
import java.text.*;
import java.util.*;
import javax.servlet.http.*;
import org.apache.commons.collections.*;
import org.apache.commons.lang.*;
import org.apache.commons.logging.*;
import org.springframework.beans.*;
import org.springframework.beans.propertyeditors.*;
import org.springframework.validation.*;
import org.springframework.web.bind.*;
import org.springframework.web.servlet.*;
import org.springframework.web.servlet.mvc.*;
import photospace.beans.*;
import photospace.graphics.*;
import photospace.meta.*;
import photospace.search.*;
import photospace.vfs.FileSystem;

public class EditController extends AbstractFormController {

    private static final Log log = LogFactory.getLog(EditController.class);

    public static final String ROTATE_LEFT = "left";

    public static final String ROTATE_RIGHT = "right";

    private Sampler sampler = new SamplerImpl();

    private Persister persister;

    private Searcher searcher;

    private FileSystem filesystem;

    private List multiEditProperties = Arrays.asList(new String[] { "title", "description", "creator", "created", "position.latitude", "position.longitude" });

    public EditController() {
        setSessionForm(true);
        setBindOnNewForm(false);
    }

    protected void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) throws Exception {
        binder.registerCustomEditor(Double.class, new CustomNumberEditor(Double.class, new DecimalFormat("##0.000000"), true));
        binder.registerCustomEditor(Date.class, new DateEditor(new SimpleDateFormat(getMessageSourceAccessor().getMessage("format.dateTime")), true));
        binder.registerCustomEditor(String[].class, "meta.tags", new TagsEditor());
    }

    protected Object formBackingObject(HttpServletRequest request) throws Exception {
        String[] paths = request.getParameterValues("paths");
        if (paths != null) {
            EditCommand command = new EditCommand(new Meta());
            command.setPaths(paths);
            List metas = new ArrayList();
            for (int i = 0; i < command.getPaths().length; i++) {
                Meta meta = persister.getMeta(command.getPaths()[i]);
                if (meta instanceof FolderMeta) {
                    FolderMeta folder = (FolderMeta) searcher.browse(meta.getPath(), Searcher.PHOTO_CONSTRAINT, Searcher.ASCENDING, 0, 3);
                    ((FolderMeta) meta).addFiles(folder.getFiles());
                }
                metas.add(meta);
            }
            command.setMetas(metas);
            BeanWrapper wrapper = new BeanWrapperImpl(command.getMeta());
            for (Iterator p = multiEditProperties.iterator(); p.hasNext(); ) {
                setSharedValue(wrapper, metas, (String) p.next());
            }
            command.setSharedKeywords(getSharedKeywords(metas));
            command.getMeta().setTags((String[]) command.getSharedKeywords().toArray(new String[] {}));
            return command;
        }
        throw new ServletRequestBindingException("Required parameter 'paths' was not supplied");
    }

    private Set getSharedKeywords(List metas) {
        HashSet shared = new HashSet(Arrays.asList(((Meta) metas.get(0)).getTags()));
        for (Iterator tags = metas.iterator(); tags.hasNext(); ) {
            Meta meta = (Meta) tags.next();
            shared.retainAll(Arrays.asList(meta.getTags()));
        }
        return shared;
    }

    protected ModelAndView processFormSubmission(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {
        if (errors.hasErrors()) {
            logger.debug("Data binding errors: " + errors.getErrorCount());
            return showForm(request, response, errors);
        } else {
            logger.debug("No errors -> processing submit");
            return onSubmit(request, response, command, errors);
        }
    }

    protected ModelAndView showForm(HttpServletRequest request, HttpServletResponse response, BindException errors) throws Exception {
        if (isSessionForm()) request.getSession().setAttribute(getFormSessionAttributeName(), errors.getTarget());
        return new ModelAndView("/edit", errors.getModel());
    }

    private void setSharedValue(BeanWrapper bean, List metas, String property) {
        Set values = new HashSet();
        CollectionUtils.collect(metas, new PropertyTransformer(property), values);
        if (values.size() == 1) bean.setPropertyValue(property, values.iterator().next());
    }

    protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {
        EditCommand form = (EditCommand) command;
        if (!StringUtils.isEmpty(form.getRotate())) {
            int degrees;
            if (ROTATE_LEFT.equals(form.getRotate())) degrees = -90; else if (ROTATE_RIGHT.equals(form.getRotate())) degrees = 90; else degrees = 0;
            log.debug("Rotating " + Arrays.asList(form.getPaths()) + " " + form.getRotate());
            for (Iterator i = form.getMetas().iterator(); i.hasNext(); ) {
                Meta meta = (Meta) i.next();
                if (!(meta instanceof PhotoMeta)) continue;
                meta = persister.getMeta(meta.getPath());
                persister.saveMeta(meta.getPath(), meta);
                File file = filesystem.getFile(meta.getPath());
                try {
                    BufferedImage image = ImageIO.read(file);
                    ImageIO.write(sampler.rotate(image, degrees), file);
                } catch (IOException e) {
                    log.warn("Exception rotation " + meta, e);
                    request.setAttribute("error", getMessageSourceAccessor().getMessage("error.rotate.failed", new Object[] { meta.getLabel() }));
                    break;
                }
            }
            form.setRotate(null);
        } else {
            log.debug("Editing " + Arrays.asList(form.getPaths()));
            BeanWrapper edited = new BeanWrapperImpl(form.getMeta());
            for (Iterator i = form.getMetas().iterator(); i.hasNext(); ) {
                Meta meta = (Meta) i.next();
                BeanWrapper bean = new BeanWrapperImpl(meta);
                for (Iterator p = multiEditProperties.iterator(); p.hasNext(); ) {
                    String property = (String) p.next();
                    Object value = edited.getPropertyValue(property);
                    if (!form.isMultiEdit()) bean.setPropertyValue(property, value); else if (value == null || (value instanceof String && StringUtils.isEmpty((String) value))) continue; else bean.setPropertyValue(property, value);
                }
                if (form.isMultiEdit()) {
                    List tags = new ArrayList(Arrays.asList(meta.getTags()));
                    tags.removeAll(form.getRemovedKeywords());
                    tags.addAll(Arrays.asList(form.getMeta().getTags()));
                    meta.setTags((String[]) tags.toArray(new String[] {}));
                } else {
                    meta.setTags(form.getMeta().getTags());
                }
                persister.saveMeta(meta.getPath(), meta);
            }
        }
        request.setAttribute("message", getMessageSourceAccessor().getMessage("message.edit.success"));
        return new ModelAndView("/edit", errors.getModel());
    }

    public void setPersister(Persister persister) {
        this.persister = persister;
    }

    public void setFilesystem(FileSystem filesystem) {
        this.filesystem = filesystem;
    }

    public void setSearcher(Searcher searcher) {
        this.searcher = searcher;
    }
}
