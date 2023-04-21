package org.dms.wicket.repository.panel;

import java.io.IOException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import javax.jcr.PathNotFoundException;
import javax.jcr.RepositoryException;
import org.apache.commons.lang.StringUtils;
import org.apache.wicket.PageParameters;
import org.apache.wicket.ResourceReference;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxFallbackLink;
import org.apache.wicket.extensions.ajax.markup.html.form.upload.UploadProgressBar;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.upload.FileUpload;
import org.apache.wicket.markup.html.form.upload.FileUploadField;
import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.markup.html.image.NonCachingImage;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.repeater.RepeatingView;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.lang.Bytes;
import org.dms.wicket.repository.db.model.FileDescription;
import org.dms.wicket.repository.file.service.JcrFileMetadata;
import org.dms.wicket.repository.page.IndexPage;
import org.dms.wicket.repository.page.UploadFilePage;
import org.dms.wicket.repository.page.admin.JcrVersionPage;
import org.xaloon.wicket.component.repository.FileRepository;
import org.xaloon.wicket.component.resource.FileResource;
import org.xaloon.wicket.component.uploadify.UploadifyBehaviorItem;
import org.xaloon.wicket.component.uploadify.UploadifyPanel;

public class RepositoryPanel extends org.apache.wicket.markup.html.panel.Panel {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    @SpringBean
    private FileRepository fileRepository;

    @SpringBean
    private JcrFileMetadata fileMetadata;

    public static final String PARENT_NAME = "photo/upload/test/";

    public RepositoryPanel(String id, PageParameters params) {
        super(id);
        String param = "";
        String name = PARENT_NAME;
        if (params.containsKey("i")) {
            param = params.getString("i");
            param = param.replaceAll("\\.", "/");
            name = PARENT_NAME + param;
        }
        final FeedbackPanel uploadFeedback = new FeedbackPanel("uploadFeedback");
        add(uploadFeedback);
        final WebMarkupContainer imageContainer = new WebMarkupContainer("imageContainer");
        imageContainer.setOutputMarkupId(true);
        add(imageContainer);
        if ((name != null) && (fileRepository.existsFile(name))) {
            imageContainer.add(new NonCachingImage("img", new FileResource(name)));
        } else {
            imageContainer.add(new NonCachingImage("img", new ResourceReference(IndexPage.class, "images/testimonial-bg.gif")));
        }
        final FileUploadForm ajaxSimpleUploadForm = new FileUploadForm("ajax-simpleUpload", name, param);
        FileUploadField fuf = new FileUploadField("fuf");
        fuf.setOutputMarkupId(true);
        UploadifyBehaviorItem uploadify = new UploadifyBehaviorItem(UploadFilePage.class);
        fuf.add(uploadify);
        ajaxSimpleUploadForm.add(fuf);
        ajaxSimpleUploadForm.add(new UploadifyPanel("uploadify", UploadFilePage.class));
        ajaxSimpleUploadForm.add(new UploadProgressBar("progress", ajaxSimpleUploadForm));
        add(ajaxSimpleUploadForm);
        final WebMarkupContainer listFiles = new WebMarkupContainer("listFiles");
        listFiles.setOutputMarkupId(true);
        reload(name, listFiles, imageContainer);
        add(listFiles);
        final WebMarkupContainer listFolders = new WebMarkupContainer("listFolders");
        listFolders.setOutputMarkupId(true);
        reloadFolder(name, param, listFolders);
        add(listFolders);
        String key = param;
        boolean visible = params.containsKey("i");
        if (param.lastIndexOf("/") > 0) {
            key = param.substring(0, param.lastIndexOf("/"));
        } else {
            key = "";
        }
        params.put("i", key);
        add((new BookmarkablePageLink<Void>("up", IndexPage.class, params)).setVisible(visible));
    }

    private void reloadFolder(final String path, final String param, final WebMarkupContainer listFolders) {
        listFolders.removeAll();
        List<String> items = fileRepository.searchFolders(path);
        Collections.sort(items, new Comparator<String>() {

            public int compare(String arg0, String arg1) {
                return arg0.compareTo(arg1);
            }
        });
        RepeatingView contents = new RepeatingView("content");
        listFolders.add(contents);
        for (final String desc : items) {
            WebMarkupContainer inner = new WebMarkupContainer(contents.newChildId());
            PageParameters params = new PageParameters();
            final String folderName = param + ((!StringUtils.isEmpty(param)) ? "/" : "") + desc;
            params.put("i", folderName.replaceAll("/", "."));
            BookmarkablePageLink<Void> link = new BookmarkablePageLink<Void>("link", IndexPage.class, params);
            link.add(new Label("name", desc));
            inner.add(link);
            AjaxFallbackLink<Void> delete = new AjaxFallbackLink<Void>("delete") {

                private static final long serialVersionUID = 1L;

                @Override
                public void onClick(AjaxRequestTarget target) {
                    try {
                        fileRepository.delete("/" + path + ((!StringUtils.isEmpty(param)) ? "/" : "") + desc);
                    } catch (PathNotFoundException e) {
                        e.printStackTrace();
                    } catch (RepositoryException e) {
                        e.printStackTrace();
                    }
                    reloadFolder(path, param, listFolders);
                    target.addComponent(listFolders);
                }
            };
            delete.add(new Image("img", new ResourceReference(IndexPage.class, "images/cross.png")));
            inner.add(delete);
            contents.add(inner);
        }
    }

    private void reload(final String path, final WebMarkupContainer listFiles, final WebMarkupContainer imageContainer) {
        listFiles.removeAll();
        List<FileDescription> items = fileRepository.searchFiles(path);
        Collections.sort(items, new Comparator<FileDescription>() {

            public int compare(FileDescription o1, FileDescription o2) {
                return o2.getLastModified().compareTo(o1.getLastModified());
            }
        });
        RepeatingView contents = new RepeatingView("content");
        listFiles.add(contents);
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        for (final FileDescription desc : items) {
            WebMarkupContainer inner = new WebMarkupContainer(contents.newChildId());
            AjaxFallbackLink<Void> show = new AjaxFallbackLink<Void>("show") {

                private static final long serialVersionUID = 1L;

                @Override
                public void onClick(AjaxRequestTarget target) {
                    imageContainer.removeAll();
                    imageContainer.add(new NonCachingImage("img", new FileResource(desc.getPath().substring(1))));
                    target.addComponent(imageContainer);
                }
            };
            show.add(new Label("name", desc.getName()));
            inner.add(show);
            inner.add(new Label("modified", df.format(desc.getLastModified())));
            inner.add(new Label("size", formatBytes(desc.getSize())));
            AjaxFallbackLink<Void> delete = new AjaxFallbackLink<Void>("delete") {

                private static final long serialVersionUID = 1L;

                @Override
                public void onClick(AjaxRequestTarget target) {
                    String jrpath = desc.getPath();
                    try {
                        fileRepository.delete(jrpath);
                    } catch (PathNotFoundException e) {
                        e.printStackTrace();
                    } catch (RepositoryException e) {
                        e.printStackTrace();
                    }
                    reload(path, listFiles, imageContainer);
                    target.addComponent(listFiles);
                }
            };
            delete.add(new Image("img", new ResourceReference(IndexPage.class, "images/cross.png")));
            inner.add(delete);
            contents.add(inner);
        }
    }

    private String formatBytes(float size) {
        NumberFormat nf = new DecimalFormat("#.00");
        if (size >= 1073741824) {
            return nf.format(size / 1073741824) + " Gb";
        } else if (size >= 1048576) {
            return nf.format(size / 1048576) + " Mb";
        } else if (size >= 1024) {
            return nf.format(size / 1024) + " KB";
        } else {
            return nf.format(size) + " bytes";
        }
    }

    private class FileUploadForm extends Form<Void> {

        private FileUploadField fileUploadField;

        private String path, param;

        public FileUploadForm(String id, String path, String param) {
            super(id);
            this.path = path;
            this.param = param;
            setMultiPart(true);
            add(fileUploadField = new FileUploadField("fileInput"));
            setMaxSize(Bytes.megabytes(10));
        }

        /**
		 * 
		 */
        private static final long serialVersionUID = 1L;

        @Override
        protected void onSubmit() {
            final FileUpload upload = fileUploadField.getFileUpload();
            try {
                if (upload != null) {
                    String mimetype = upload.getContentType();
                    fileMetadata.storeFileVersion(path, upload.getClientFileName(), mimetype, upload.getInputStream());
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            setRedirect(true);
            PageParameters params = new PageParameters();
            if (upload != null) {
                params.put("i", param.replaceAll("/", "."));
            }
            setResponsePage(IndexPage.class, params);
        }
    }
}
