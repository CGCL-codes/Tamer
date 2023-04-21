package com.gwtaf.core.client.widget.richtext.images;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;

/**
 * This {@link ClientBundle} is used for all the button icons. Using an image bundle allows all of these
 * images to be packed into a single image, which saves a lot of HTTP requests, drastically improving startup
 * time.
 */
public interface Images extends ClientBundle {

    public static final Images INSTANCE = GWT.create(Images.class);

    @Source("bold.gif")
    public ImageResource bold();

    @Source("createLink.gif")
    public ImageResource createLink();

    @Source("hr.gif")
    public ImageResource hr();

    @Source("indent.gif")
    public ImageResource indent();

    @Source("insertImage.gif")
    public ImageResource insertImage();

    @Source("italic.gif")
    public ImageResource italic();

    @Source("justifyCenter.gif")
    public ImageResource justifyCenter();

    @Source("justifyLeft.gif")
    public ImageResource justifyLeft();

    @Source("justifyRight.gif")
    public ImageResource justifyRight();

    @Source("ol.gif")
    public ImageResource ol();

    @Source("outdent.gif")
    public ImageResource outdent();

    @Source("removeFormat.gif")
    public ImageResource removeFormat();

    @Source("removeLink.gif")
    public ImageResource removeLink();

    @Source("strikeThrough.gif")
    public ImageResource strikeThrough();

    @Source("subscript.gif")
    public ImageResource subscript();

    @Source("superscript.gif")
    public ImageResource superscript();

    @Source("ul.gif")
    public ImageResource ul();

    @Source("underline.gif")
    public ImageResource underline();
}
