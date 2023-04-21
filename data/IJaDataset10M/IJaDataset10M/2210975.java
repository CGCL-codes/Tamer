package com.appspot.gwtsimple.client;

import com.google.gwt.canvas.client.Canvas;
import com.google.gwt.canvas.dom.client.Context2d;
import com.google.gwt.canvas.dom.client.CssColor;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.ImageElement;
import com.google.gwt.dom.client.Touch;
import com.google.gwt.event.dom.client.*;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;

public class GwtSimple implements EntryPoint {

    static final String holderId = "canvasholder";

    static final String upgradeMessage = "Your browser does not support the HTML5 Canvas. Please upgrade your browser to view this demo.";

    Canvas canvas;

    Canvas backBuffer;

    int mouseX, mouseY;

    static final int refreshRate = 25;

    static final int height = 400;

    static final int width = 400;

    private final CssColor redrawColor = CssColor.make("rgba(155,255,255,1)");

    private Context2d context;

    private Context2d backBufferContext;

    private Image logoGWT;

    private Image logoGAE;

    private ImageElement imageElementGWT;

    private ImageElement imageElementGAE;

    private int halfHeight;

    private int halfWidth;

    private int x, y;

    private ClickHandlerServiceAsync clickHandlerSrv;

    Label titleLabel = new Label();

    Label errorMsgLabel = new Label();

    public void onModuleLoad() {
        RootPanel.get().add(titleLabel);
        errorMsgLabel.setVisible(false);
        canvas = Canvas.createIfSupported();
        backBuffer = Canvas.createIfSupported();
        if (canvas == null) {
            RootPanel.get(holderId).add(new Label(upgradeMessage));
            return;
        }
        clickHandlerSrv = GWT.create(ClickHandlerService.class);
        canvas.setWidth(width + "px");
        canvas.setHeight(height + "px");
        canvas.setCoordinateSpaceWidth(width);
        canvas.setCoordinateSpaceHeight(height);
        backBuffer.setCoordinateSpaceWidth(width);
        backBuffer.setCoordinateSpaceHeight(height);
        RootPanel.get().add(canvas);
        context = canvas.getContext2d();
        backBufferContext = backBuffer.getContext2d();
        backBufferContext.setFillStyle(redrawColor);
        backBufferContext.fillRect(0, 0, width, height);
        logoGWT = new Image("gwtlogo.png");
        logoGWT.setVisible(false);
        RootPanel.get().add(logoGWT);
        imageElementGWT = (ImageElement) logoGWT.getElement().cast();
        logoGAE = new Image("appengine.png");
        logoGAE.setVisible(false);
        RootPanel.get().add(logoGAE);
        imageElementGAE = (ImageElement) logoGAE.getElement().cast();
        halfWidth = 40;
        halfHeight = 40;
        RootPanel.get().add(errorMsgLabel);
        errorMsgLabel.setStyleName("errorMessage");
        errorMsgLabel.setVisible(false);
        initHandlers();
    }

    void doUpdate() {
        context.drawImage(backBufferContext.getCanvas(), 0, 0);
    }

    void initHandlers() {
        canvas.addMouseMoveHandler(new MouseMoveHandler() {

            public void onMouseMove(MouseMoveEvent event) {
                mouseX = event.getRelativeX(canvas.getElement());
                mouseY = event.getRelativeY(canvas.getElement());
            }
        });
        canvas.addMouseOutHandler(new MouseOutHandler() {

            public void onMouseOut(MouseOutEvent event) {
                mouseX = -200;
                mouseY = -200;
            }
        });
        canvas.addTouchMoveHandler(new TouchMoveHandler() {

            public void onTouchMove(TouchMoveEvent event) {
                event.preventDefault();
                if (event.getTouches().length() > 0) {
                    Touch touch = event.getTouches().get(0);
                    mouseX = touch.getRelativeX(canvas.getElement());
                    mouseY = touch.getRelativeY(canvas.getElement());
                }
                event.preventDefault();
            }
        });
        canvas.addTouchEndHandler(new TouchEndHandler() {

            public void onTouchEnd(TouchEndEvent event) {
                event.preventDefault();
                mouseX = -200;
                mouseY = -200;
            }
        });
        canvas.addGestureStartHandler(new GestureStartHandler() {

            public void onGestureStart(GestureStartEvent event) {
                event.preventDefault();
            }
        });
        canvas.addClickHandler(new ClickHandler() {

            public void onClick(ClickEvent event) {
                x = event.getX();
                y = event.getY();
                AsyncCallback<String> callback = new AsyncCallback<String>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        errorMsgLabel.setText("Error: " + caught.getMessage());
                        errorMsgLabel.setVisible(true);
                    }

                    @Override
                    public void onSuccess(String result) {
                        backBufferContext.setFillStyle(redrawColor);
                        backBufferContext.fillRect(0, 0, width, height);
                        ImageElement imageElement;
                        if (result.equals("GWT")) {
                            imageElement = imageElementGWT;
                        } else {
                            imageElement = imageElementGAE;
                        }
                        backBufferContext.drawImage(imageElement, x - halfWidth, y - halfHeight);
                        context.drawImage(backBufferContext.getCanvas(), 0, 0);
                    }
                };
                clickHandlerSrv.click(x, y, callback);
            }
        });
    }
}
