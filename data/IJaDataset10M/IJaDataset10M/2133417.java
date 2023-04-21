package com.acv.webapp.taglib;

import java.io.IOException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.TagSupport;
import org.apache.log4j.Logger;
import org.apache.struts.util.MessageResources;
import com.acv.dao.catalog.Browsable;
import com.acv.dao.catalog.Displayable;
import com.acv.dao.catalog.Media;
import com.acv.dao.common.Constants;

/**
 * This tag generate html code that display an image or a browser of multiple image.
 *
 */
public final class ImageBrowserTag extends TagSupport {

    private static final long serialVersionUID = 1L;

    private static final Logger log = Logger.getLogger(ImageBrowserTag.class);

    private static MessageResources messages = MessageResources.getMessageResources("ApplicationResources");

    /** a Media,Set,Displayable or Browsable object with one or multi Media */
    private Object value = null;

    /** set browser, if true display a browser(multi image) else a simple image */
    private boolean browser = false;

    /** if true, display the "no image" logo when no image define in database */
    private boolean displayNoImage = true;

    /** set width html size*/
    private String width = null;

    /** set height html size*/
    private String height = null;

    /** set the html css class */
    private String cssclass = null;

    /** set type */
    private String type = null;

    /** set tour360, url id for tour360 */
    private String tour360 = null;

    /** if you want only one image and give a set in parameter, you can here specifie the wanted id */
    private String mediaId = null;

    /** retrive sentence for photo link in properties international files */
    private String photo = "";

    /** retrive sentence for video link in properties international files */
    private String video = "";

    /** retrive sentence for t360 link in properties international files */
    private String t360 = "";

    /** set small, define if image will be diplayed in full or small size */
    private boolean small = true;

    /**
	 * set the object that contain media witch need to be displayed
	 * @param value, a Media,Set,Displayable or Browsable object with one or multi Media
	 */
    public void setValue(Object value) {
        this.value = value;
    }

    /**
	 * Return html string for an image browser un full size
	 * @param lang language of the image and the browser
	 * @param mediaOverView set of media to be displayed
	 * @param mediaVideo set of video to be linked
	 * @return html string
	 */
    public String imageBrowser(String lang, Set<Media> mediaOverView, Set<Media> mediaVideo) {
        StringBuilder sb = new StringBuilder();
        boolean isBrowserNeeded = false;
        boolean containsAtLeastOneImage = false;
        if (width == null) {
            width = Constants.MEDIA_SLIDESHOW_DEFAULT_WIDTH;
        }
        if (height == null) {
            height = Constants.MEDIA_SLIDESHOW_DEFAULT_HEIGHT;
        }
        if ((mediaOverView == null) || (mediaOverView.isEmpty())) {
            if (displayNoImage) {
                sb.append("<div class=\"imageBrowser clearfix\">");
                sb.append("<ul>");
                sb.append("<li>");
                sb.append("<img src=\"" + Constants.MEDIA_NO_IMAGE + "\" width=\"" + width + "\" height=\"" + height + "\" alt=\"\" />");
                sb.append("</li>");
                sb.append("</ul>");
                sb.append("</div>");
            }
        } else {
            int mediaCount = 0;
            for (Media media : mediaOverView) {
                boolean noImage = false;
                String smallpath = media.getPathSmallSizeImage();
                String smallAlt = media.getSmallMetadata().get("ALT_".concat(lang).toUpperCase());
                if (smallAlt == null) {
                    smallAlt = "";
                }
                String fullpath = media.getPathNormalSizeImage();
                if (smallpath == null || "".equalsIgnoreCase(smallpath)) {
                    smallpath = Constants.MEDIA_SMALL_NO_IMAGE;
                    noImage = true;
                }
                if (noImage && !displayNoImage) continue;
                String fullTitle = media.getNormalMetadata().get("TITLE_".concat(lang).toUpperCase());
                String fullSubtitle = media.getNormalMetadata().get("DESC_".concat(lang).toUpperCase());
                String fullAlt = media.getNormalMetadata().get("ALT_".concat(lang).toUpperCase());
                String fullWidth = media.getNormalMetadata().get("WIDTH");
                String fullHeight = media.getNormalMetadata().get("HEIGHT");
                if (fullpath == null || "".equalsIgnoreCase(fullpath)) {
                    fullpath = smallpath;
                    fullTitle = media.getSmallMetadata().get("TITLE_".concat(lang).toUpperCase());
                    fullSubtitle = media.getSmallMetadata().get("DESC_".concat(lang).toUpperCase());
                    fullAlt = media.getSmallMetadata().get("ALT_".concat(lang).toUpperCase());
                    fullWidth = media.getSmallMetadata().get("WIDTH");
                    fullHeight = media.getSmallMetadata().get("HEIGHT");
                }
                if (fullWidth == null || "".equalsIgnoreCase(fullWidth)) {
                    fullWidth = Constants.MEDIA_SLIDESHOW_DEFAULT_WIDTH;
                }
                if (fullHeight == null || "".equalsIgnoreCase(fullHeight)) {
                    fullHeight = Constants.MEDIA_SLIDESHOW_DEFAULT_HEIGHT;
                }
                if (fullAlt == null || "".equalsIgnoreCase(fullAlt)) {
                    fullAlt = "PhotoBigHolder";
                }
                if (fullTitle == null) {
                    fullTitle = "";
                }
                if (fullSubtitle == null) {
                    fullSubtitle = "";
                }
                if (!containsAtLeastOneImage) {
                    sb.append("<div class=\"imageBrowser clearfix\">");
                    sb.append("<ul>");
                }
                containsAtLeastOneImage = true;
                mediaCount++;
                sb.append("<li>");
                sb.append("<a onclick=\"imageBrowser.openPicture(this); return false\" href=\"#\"><img src=\"" + smallpath + "\" width=\"" + width + "\" height=\"" + height + "\" alt=\"" + smallAlt + "\" /></a>");
                sb.append("<div class=\"liFullPhoto\">");
                sb.append("<span class=\"title\">" + fullTitle + "</span>");
                sb.append("<span class=\"content\">" + fullSubtitle + "</span>");
                sb.append("<img src=\"" + fullpath + "\" height=" + fullHeight + " width=" + fullWidth + " alt=\"" + fullAlt + "\" />");
                sb.append("</div>");
                sb.append("</li>");
            }
            if (containsAtLeastOneImage) sb.append("</ul>");
            if (mediaCount > 1) isBrowserNeeded = true;
        }
        if (containsAtLeastOneImage) {
            sb.append("<a href=\"#\" onclick=\"imageBrowser.openPicture($('div.imageBrowser ul li:eq('+imageBrowser.currentItem+') a')); return false;\" class=\"plusPhoto\" title=\"View this photo in high resolution\">&nbsp;</a>");
        }
        if ((isBrowserNeeded) || ((mediaVideo != null) && (mediaVideo.iterator().next().getPathSmallSizeImage() != null)) || ((tour360 != null) && (tour360.length() > 0))) {
            sb.append("<div>");
            if (mediaVideo != null) {
                String smallpath = mediaVideo.iterator().next().getPathSmallSizeImage();
                if (smallpath != null) {
                    sb.append("<a class=\"fLeft\" href=\"" + smallpath + "\">" + video + "</a>");
                }
            }
            if ((tour360 != null) && (tour360.length() > 0)) {
                sb.append("<a class=\"fLeft marginLeftBig\" href=\"" + tour360 + "\">" + t360 + "</a>");
            }
            if ((mediaOverView != null) && (mediaOverView.size() > 1)) {
                sb.append("<span class=\"photoBrowser\">");
                sb.append("<a class=\"fRight photoArrowRight\" onclick=\"imageBrowser.next(); return false;\" href=\"#\"></a>");
                sb.append("<span class=\"fRight counter\">0/0</span>");
                sb.append("<a class=\"fRight photoArrowLeft\" onclick=\"imageBrowser.previous(); return false;\" href=\"#\"></a>");
                sb.append("<span class=\"fRight\">" + photo + "</span>");
                sb.append("</span>");
            }
            sb.append("</div>");
        }
        if (containsAtLeastOneImage) sb.append("</div>");
        sb.append("<script type=\"text/javascript\">");
        sb.append("$(document).ready( function() {");
        sb.append("imageBrowser.init();");
        sb.append("});");
        sb.append("</script>");
        return sb.toString();
    }

    /**
	 * Return html string for an image browser un small size
	 * @param lang language of the image and the browser
	 * @param mediaOverView set of media to be displayed
	 * @return html string
	 */
    public String smallImageBrowser(String lang, Set<Media> mediaOverView) {
        StringBuilder sb = new StringBuilder();
        boolean containsAtLeastOneImage = false;
        boolean isBrowserNeeded = false;
        if (width == null) {
            width = Constants.MEDIA_SMALL_SLIDESHOW_DEFAULT_WIDTH;
        }
        if (height == null) {
            height = Constants.MEDIA_SMALL_SLIDESHOW_DEFAULT_HEIGHT;
        }
        if ((mediaOverView == null) || (mediaOverView.isEmpty())) {
            if (displayNoImage) {
                sb.append("<div class=\"smallImageBrowser clearfix\">");
                sb.append("<ul>");
                sb.append("<li>");
                sb.append("<img src=\"" + Constants.MEDIA_SMALL_NO_IMAGE + "\" width=\"" + width + "\" height=\"" + height + "\" />");
                sb.append("</li>");
                sb.append("</ul>");
                sb.append("</div>");
            }
        } else {
            int mediaCount = 0;
            for (Media media : mediaOverView) {
                boolean noImage = false;
                String smallpath = media.getPathSmallSizeImage();
                if (smallpath == null || "".equalsIgnoreCase(smallpath)) {
                    smallpath = Constants.MEDIA_SMALL_NO_IMAGE;
                    noImage = true;
                }
                if (noImage && !displayNoImage) continue;
                if (!containsAtLeastOneImage) {
                    sb.append("<div class=\"smallImageBrowser clearfix\">");
                    sb.append("<ul>");
                }
                containsAtLeastOneImage = true;
                mediaCount++;
                sb.append("<li>");
                sb.append("<img src=\"" + smallpath + "\" width=\"" + width + "\" height=\"" + height + "\" />");
                sb.append("</li>");
            }
            if (containsAtLeastOneImage) sb.append("</ul>");
            if (mediaCount > 1) isBrowserNeeded = true;
        }
        if (isBrowserNeeded) {
            sb.append("<div>");
            sb.append("<span class=\"photoBrowser\">");
            sb.append("<a class=\"fRight photoArrowRight\" onclick=\"smallImageBrowser.next(this); return false;\" href=\"#\"></a>");
            sb.append("<span class=\"fRight counter\">0/0</span>");
            sb.append("<a class=\"fRight photoArrowLeft\" onclick=\"smallImageBrowser.previous(this); return false;\" href=\"#\"></a>");
            sb.append("<span class=\"fRight\">" + photo + "</span>");
            sb.append("</span>");
            sb.append("</div>");
        }
        if (containsAtLeastOneImage) sb.append("</div>");
        return sb.toString();
    }

    /**
	 * Return html string for a simple image in full size
	 * @param lang language of the image and the browser
	 * @param media set of media to be displayed
	 * @return html string
	 */
    public String simpleImage(String lang, Media media) {
        StringBuilder sb = new StringBuilder();
        if (small) {
            if (media.getPathSmallSizeImage() == null || "".equalsIgnoreCase(media.getPathSmallSizeImage())) {
                return noImage(lang);
            }
            sb.append("<img src=\"" + media.getPathSmallSizeImage() + "\"");
            sb.append(" alt =\"" + media.getSmallMetadata().get("ALT_".concat(lang)) + "\"");
        } else {
            if (media.getPathNormalSizeImage() == null || "".equalsIgnoreCase(media.getPathNormalSizeImage())) {
                return noImage(lang);
            }
            sb.append("<img src=" + media.getPathNormalSizeImage());
            sb.append(" alt =\"" + media.getNormalMetadata().get("ALT_".concat(lang)) + "\"");
        }
        if (cssclass != null) {
            sb.append(" class=\"" + cssclass + "\"");
        }
        if (width != null) {
            sb.append(" width=" + width);
        } else {
            if (small) {
                sb.append(" width =" + media.getSmallMetadata().get("WIDTH") + "");
            } else {
                sb.append(" width =" + media.getNormalMetadata().get("WIDTH") + "");
            }
        }
        if (height != null) {
            sb.append(" height=" + height);
        } else {
            if (small) {
                sb.append(" height =" + media.getSmallMetadata().get("HEIGHT") + "");
            } else {
                sb.append(" height =" + media.getNormalMetadata().get("HEIGHT") + "");
            }
        }
        sb.append("/>");
        return sb.toString();
    }

    /**
	 * Return html string for display logo "no image"
	 * @param lang language of the image and the browser
	 * @return html string
	 */
    public String noImage(String lang) {
        StringBuilder sb = new StringBuilder();
        if (displayNoImage) {
            if (cssclass != null) {
                sb.append("<div ");
                sb.append("class=\"" + cssclass + "\" ");
                sb.append(">");
            }
            sb.append("<div class=\"genericImage\" ");
            sb.append("style=\"");
            if (width != null) {
                sb.append(" width:" + width + "px;");
            } else {
                sb.append(" width:" + Constants.MEDIA_SMALL_NO_IMAGE_DEFAULT_WIDTH + "px;");
            }
            if (height != null) {
                sb.append(" height:" + height + "px;");
            } else {
                sb.append(" height:" + Constants.MEDIA_SMALL_NO_IMAGE_DEFAULT_HEIGHT + "px;");
            }
            sb.append("\">");
            sb.append("<div class=\"caption\"></div>");
            sb.append("</div>");
            if (cssclass != null) {
                sb.append("</div>");
            }
        }
        return sb.toString();
    }

    /**
	 * @see javax.servlet.jsp.tagext.TagSupport#doStartTag()
	 */
    public int doStartTag() throws JspException {
        Locale locale = (Locale) pageContext.getAttribute(Constants.PREFERRED_LOCALE_KEY, PageContext.SESSION_SCOPE);
        String lang = locale.getLanguage().toUpperCase();
        Locale toMessageResources = new Locale(lang);
        Map<String, Map<String, Set<Media>>> media = null;
        Set<Media> mediaOverView = null;
        Set<Media> mediaVideo = null;
        Set<Media> mediaType = null;
        if (messages != null) {
            photo = messages.getMessage(toMessageResources, "imageBrowser.photos");
            video = messages.getMessage(toMessageResources, "imageBrowser.video");
            t360 = messages.getMessage(toMessageResources, "imageBrowser.360");
        }
        if (browser) {
            try {
                if (value == null) {
                    if (small) {
                        pageContext.getOut().write(smallImageBrowser(lang, mediaOverView));
                        return EVAL_PAGE;
                    } else {
                        pageContext.getOut().write(imageBrowser(lang, mediaOverView, mediaVideo));
                        return EVAL_PAGE;
                    }
                } else if (value instanceof Set) {
                    media = new HashMap<String, Map<String, Set<Media>>>();
                    for (Object object : (Set) value) {
                        if (object instanceof Displayable) {
                            media.putAll(((Displayable) object).getMediasWithType());
                        } else if (object instanceof Browsable) {
                            media.putAll(((Browsable) object).getMediasWithType());
                        }
                    }
                } else if (value instanceof Displayable) {
                    media = ((Displayable) value).getMediasWithType();
                } else if (value instanceof Browsable) {
                    media = ((Browsable) value).getMediasWithType();
                }
                if (type != null) {
                    if (media.get(lang) != null) {
                        mediaType = media.get(lang).get(type);
                    } else if (media.get(Constants.DEFAULT_CONTENT_LANGUAGE_CODE) != null) {
                        mediaType = media.get(Constants.DEFAULT_CONTENT_LANGUAGE_CODE).get(type);
                    }
                    if (small) {
                        pageContext.getOut().write(smallImageBrowser(lang, mediaType));
                        return EVAL_PAGE;
                    } else {
                        pageContext.getOut().write(imageBrowser(lang, mediaType, null));
                        return EVAL_PAGE;
                    }
                }
                if (media.get(lang) != null) {
                    mediaOverView = media.get(lang).get(Constants.MEDIA_OVERVIEW);
                    mediaVideo = media.get(lang).get(Constants.MEDIA_VIDEO);
                } else if (media.get(Constants.DEFAULT_CONTENT_LANGUAGE_CODE) != null) {
                    mediaOverView = media.get(Constants.DEFAULT_CONTENT_LANGUAGE_CODE).get(Constants.MEDIA_OVERVIEW);
                    mediaVideo = media.get(Constants.DEFAULT_CONTENT_LANGUAGE_CODE).get(Constants.MEDIA_VIDEO);
                }
                if (small) {
                    pageContext.getOut().write(smallImageBrowser(lang, mediaOverView));
                    return EVAL_PAGE;
                } else {
                    pageContext.getOut().write(imageBrowser(lang, mediaOverView, mediaVideo));
                    return EVAL_PAGE;
                }
            } catch (Exception e) {
                try {
                    if (small) {
                        pageContext.getOut().write(smallImageBrowser(lang, null));
                        return EVAL_PAGE;
                    } else {
                        pageContext.getOut().write(imageBrowser(lang, null, null));
                        return EVAL_PAGE;
                    }
                } catch (Exception e1) {
                    return EVAL_PAGE;
                }
            }
        } else {
            try {
                if (value == null) {
                    pageContext.getOut().write(noImage(lang));
                    return EVAL_PAGE;
                } else if (value instanceof Media) {
                    Media myMedia = (Media) value;
                    pageContext.getOut().write(simpleImage(lang, myMedia));
                    return EVAL_PAGE;
                } else if (value instanceof Set) {
                    media = new HashMap<String, Map<String, Set<Media>>>();
                    for (Object object : (Set) value) {
                        if (object instanceof Media) {
                            Media myMedia = (Media) object;
                            pageContext.getOut().write(simpleImage(lang, myMedia));
                            return EVAL_PAGE;
                        }
                    }
                } else if (value instanceof Displayable) {
                    media = ((Displayable) value).getMediasWithType();
                } else if (value instanceof Browsable) {
                    media = ((Browsable) value).getMediasWithType();
                } else {
                    pageContext.getOut().write(noImage(lang));
                    return EVAL_PAGE;
                }
                if (type != null) {
                    if (media.get(lang) != null) {
                        mediaType = media.get(lang).get(type);
                    } else if (media.get(Constants.DEFAULT_CONTENT_LANGUAGE_CODE) != null) {
                        mediaType = media.get(Constants.DEFAULT_CONTENT_LANGUAGE_CODE).get(type);
                    }
                    if (mediaType != null) {
                        for (Media mediaWithType : mediaType) {
                            if (mediaId != null) {
                                if (mediaId.equals(mediaWithType.getMediaId().toString())) {
                                    pageContext.getOut().write(simpleImage(lang, mediaWithType));
                                    return EVAL_PAGE;
                                }
                            } else {
                                pageContext.getOut().write(simpleImage(lang, mediaWithType));
                                return EVAL_PAGE;
                            }
                        }
                    }
                    pageContext.getOut().write(noImage(lang));
                    return EVAL_PAGE;
                }
            } catch (Exception e) {
                try {
                    pageContext.getOut().write(noImage(lang));
                } catch (IOException e1) {
                    return EVAL_PAGE;
                }
                return EVAL_PAGE;
            }
        }
        return EVAL_PAGE;
    }

    /**
	 * @see javax.servlet.jsp.tagext.TagSupport#doEndTag()
	 */
    public int doEndTag() throws JspException {
        return EVAL_PAGE;
    }

    /**
	 * set tour360
	 * @param tour360, url id for tour360
	 */
    public void setTour360(String tour360) {
        this.tour360 = tour360;
    }

    /**
	 * set small, define if image will be diplayed in full or small size
	 * @param small (small if not call by default)
	 */
    public void setSmall(boolean small) {
        this.small = small;
    }

    /**
	 * set height html size
	 * @param height
	 */
    public void setHeight(String height) {
        this.height = height;
    }

    /**
	 * set width html size
	 * @param width
	 */
    public void setWidth(String width) {
        this.width = width;
    }

    /**
	 * set type
	 * @param type
	 */
    public void setType(String type) {
        this.type = type;
    }

    /**
	 * set browser, if true display a browser(multi image) else a simple image
	 * @param browser (false by default if not called)
	 */
    public void setBrowser(boolean browser) {
        this.browser = browser;
    }

    /**
	 * set the html css class
	 * @param cssclass the class
	 */
    public void setCssclass(String cssclass) {
        this.cssclass = cssclass;
    }

    /**
	 * if you want only one image and give a set in parameter, you can here specifie the wanted id
	 * @param mediaId the id of the media to display
	 */
    public void setMediaId(String mediaId) {
        this.mediaId = mediaId;
    }

    /**
	 * if true, display the "no image" logo when no image define in database
	 * @param displayNoImage (true by default if not called)
	 */
    public void setDisplayNoImage(boolean displayNoImage) {
        this.displayNoImage = displayNoImage;
    }
}
