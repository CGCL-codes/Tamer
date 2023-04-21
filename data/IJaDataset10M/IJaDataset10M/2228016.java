package co.fxl.gui.impl;

import co.fxl.gui.api.IBordered.IBorder;
import co.fxl.gui.api.ICallback;
import co.fxl.gui.api.IClickable.IClickListener;
import co.fxl.gui.api.IContainer;
import co.fxl.gui.api.IGridPanel;
import co.fxl.gui.api.IHorizontalPanel;
import co.fxl.gui.api.IImage;
import co.fxl.gui.api.ILabel;
import co.fxl.gui.api.ITextField;
import co.fxl.gui.api.IUpdateable.IUpdateListener;
import co.fxl.gui.api.IVerticalPanel;

public class ViewImpl extends LazyClickListener {

    public static boolean FLIP_BEFORE = false;

    private final ViewList viewList;

    private ILabel label;

    private IGridPanel grid;

    private IHorizontalPanel labelPanel0;

    private ViewDecorator decorator;

    private FlipPage content;

    private Object bo;

    private IImage removeImage;

    private IImage image;

    private IHorizontalPanel labelPanel;

    private String imageResource;

    private IVerticalPanel basicPanel;

    private boolean disabled;

    private FlipPage sideContent;

    private ICallback<Void> oneTimeCallback;

    ViewImpl(ViewList viewList, String imageResource) {
        this(viewList, imageResource, false);
    }

    ViewImpl(ViewList viewList, String imageResource, boolean isNew) {
        this.viewList = viewList;
        this.imageResource = imageResource;
        style(viewList, isNew);
        viewList.hasLinks = true;
    }

    void style(ViewList viewList, boolean isNew) {
        basicPanel = this.viewList.panel.add().panel().vertical();
        if (viewList.hasLinks) {
            basicPanel.addSpace(3);
            IBorder border = basicPanel.border();
            border.color().rgb(172, 197, 213);
            border.style().top();
        }
        basicPanel.addSpace(2);
        grid = basicPanel.add().panel().grid();
        basicPanel.addSpace(2);
        labelPanel0 = grid.cell(0, 0).panel().horizontal();
        labelPanel = labelPanel0.add().panel().horizontal().spacing(2);
        decorate(isNew);
    }

    protected void decorate(boolean isNew) {
        if (imageResource != null) {
            image = labelPanel.addSpace(0).add().image().resource(imageResource);
            image.addClickListener(this);
            labelPanel.addSpace(2);
        } else labelPanel.addSpace(0);
        if (!isNew) {
            label = labelPanel.add().label().hyperlink();
            styleViewlistEntryInactive(label);
            label.addClickListener(this);
            labelPanel.addSpace(4);
            content = this.viewList.widget.contentPanel();
            sideContent = this.viewList.widget.sideContentPanel();
            if (this.viewList.newListener != null) {
                removeImage = grid.cell(1, 0).valign().center().width(30).align().end().panel().horizontal().addSpace(4).add().image();
                removeImage.resource(Icons.CANCEL).addClickListener(new LazyClickListener() {

                    @Override
                    public void onAllowedClick() {
                        ViewImpl.this.viewList.remove(grid, ViewImpl.this, new CallbackTemplate<Boolean>() {

                            @Override
                            public void onSuccess(Boolean result) {
                            }
                        });
                    }
                });
                removeImage.visible(false);
            }
        } else {
            final ITextField tf = labelPanel.add().textField();
            tf.focus(true);
            new Heights(0).decorate(tf);
            tf.width(199);
            final IClickListener acceptListener = new IClickListener() {

                @Override
                public void onClick() {
                    labelPanel.clear();
                    decorate(false);
                    title(tf.text().trim());
                    ViewImpl.this.viewList.newListener.onNew(ViewImpl.this, new CallbackTemplate<Void>() {

                        @Override
                        public void onSuccess(Void result) {
                            onAllowedClick();
                        }
                    });
                }
            };
            final IImage accept = labelPanel.addSpace(4).add().image().resource(Icons.ACCEPT).addClickListener(acceptListener).mouseLeft();
            tf.addKeyListener(new IClickListener() {

                @Override
                public void onClick() {
                    if (accept.clickable()) acceptListener.onClick();
                }
            }).enter();
            accept.clickable(false);
            tf.addUpdateListener(new IUpdateListener<String>() {

                @Override
                public void onUpdate(String value) {
                    accept.clickable(check(value));
                }
            });
            labelPanel.addSpace(4).add().image().resource(Icons.CANCEL).addClickListener(new IClickListener() {

                @Override
                public void onClick() {
                    ViewImpl.this.viewList.remove(grid, ViewImpl.this, null);
                }
            }).mouseLeft();
        }
    }

    private boolean check(String value) {
        if (value.trim().length() == 0) return false;
        for (int i = 0; i < value.length(); i++) if (!(Character.isLetterOrDigit(value.charAt(i)) || value.charAt(i) == ' ')) return false;
        return true;
    }

    public ViewImpl businessObject(Object bo) {
        this.bo = bo;
        return this;
    }

    public String title() {
        if (label == null) return null;
        return label.text();
    }

    public Object businessObject() {
        return bo;
    }

    public ViewImpl title(String title) {
        label.text(title);
        return this;
    }

    @Override
    public void onAllowedClick() {
        if (content == null) return;
        label.font().underline(false);
        IContainer next = content.next();
        final String resource = image.resource();
        image.resource("loading_black.gif");
        IVerticalPanel vertical = sideContent.next().panel().vertical();
        if (FLIP_BEFORE) {
            content.flip();
            sideContent.flip();
        }
        decorator.decorate(next, vertical, new CallbackTemplate<Void>(oneTimeCallback) {

            @Override
            public void onSuccess(Void result) {
                image.resource(resource);
                for (ViewList viewList : ViewImpl.this.viewList.widget.viewLists) {
                    for (ViewImpl view : viewList.views) {
                        view.clickable(view != ViewImpl.this);
                    }
                }
                if (!FLIP_BEFORE) {
                    content.flip();
                    sideContent.flip();
                }
                if (oneTimeCallback != null) {
                    ICallback<Void> cb = oneTimeCallback;
                    oneTimeCallback = null;
                    cb.onSuccess(null);
                }
            }

            @Override
            public void onFail(Throwable throwable) {
                image.resource(resource);
                super.onFail(throwable);
            }
        });
    }

    private void clickable(boolean clickable) {
        if (disabled && clickable) return;
        if (label == null) return;
        label.clickable(clickable);
        if (image != null) image.clickable(clickable);
        if (!clickable) {
            notClickable();
        } else {
            clickable();
        }
    }

    public void clickable() {
        styleViewlistEntryInactive(label);
        if (removeImage != null) {
            removeImage.visible(false);
        }
    }

    public void styleViewlistEntryInactive(ILabel label) {
        label.font().pixel(13).weight().plain();
    }

    public void notClickable() {
        styleViewlistEntryActive(label);
        if (removeImage != null && this.viewList.newListener.isRemovable(this)) {
            removeImage.visible(true);
        }
    }

    public void styleViewlistEntryActive(ILabel label) {
        label.font().pixel(13).weight().bold();
        label.font().color().black();
    }

    public ViewImpl decorator(ViewDecorator decorator) {
        this.decorator = decorator;
        return this;
    }

    public ViewImpl visible(boolean showModule) {
        if (!showModule) {
            disabled = true;
            clickable(false);
            label.clickable(false).font().weight().italic();
        }
        return this;
    }

    public boolean visible() {
        return !disabled;
    }

    public void onClick(ICallback<Void> cb) {
        oneTimeCallback = cb;
        onClick();
    }
}
