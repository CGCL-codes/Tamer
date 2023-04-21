package org.openremote.web.console.client.widget;

import org.openremote.web.console.client.Constants;
import org.openremote.web.console.client.event.OREvent;
import org.openremote.web.console.client.gxtextends.ImageSlider;
import org.openremote.web.console.client.listener.OREventListener;
import org.openremote.web.console.client.rpc.AsyncSuccessCallback;
import org.openremote.web.console.client.utils.ClientDataBase;
import org.openremote.web.console.client.utils.ORListenerManager;
import org.openremote.web.console.domain.Slider;
import com.extjs.gxt.ui.client.event.ComponentEvent;
import com.extjs.gxt.ui.client.event.DragEvent;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.layout.CenterLayout;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Image;

/**
 * The ScreenSlider is for generating slider component by slider model.
 * It support change images,send control command, polling status.
 */
public class ScreenSlider extends ScreenControl implements SensoryDelegate {

    private ImageSlider imageSlider;

    /**
    * Instantiates a new screen slider.
    * 
    * @param slider the slider
    */
    public ScreenSlider(Slider slider) {
        setComponent(slider);
        setSize(slider.getFrameWidth(), slider.getFrameHeight());
        setLayout(new CenterLayout());
        initImageSlider(slider);
        if (slider.getSensor() != null) {
            addPollingSensoryListener();
        }
    }

    /**
    * Inits the image slider, set images if there is have.
    * 
    * @param slider the slider
    */
    private void initImageSlider(final Slider slider) {
        imageSlider = new ImageSlider() {

            @Override
            protected void onDragEnd(DragEvent de) {
                super.onDragEnd(de);
                if (!slider.isPassive()) {
                    sendCommand("" + getValue());
                }
            }

            @Override
            protected void onClick(ComponentEvent ce) {
                super.onClick(ce);
                if (!slider.isPassive()) {
                    sendCommand("" + getValue());
                }
            }
        };
        imageSlider.setMinValue(slider.getMinValue());
        imageSlider.setMaxValue(slider.getMaxValue());
        imageSlider.setIncrement(1);
        if (slider.isVertical()) {
            imageSlider.setVertical(true);
            imageSlider.setHeight(slider.getFrameHeight());
        } else {
            imageSlider.setWidth(slider.getFrameWidth());
        }
        if (slider.isPassive()) {
            imageSlider.setDraggable(false);
            imageSlider.setClickToChange(false);
        }
        String resourcePath = ClientDataBase.getResourceRootPath();
        if (slider.getMinTrackImage() != null) {
            imageSlider.setMinTrackImage(resourcePath + slider.getMinTrackImage().getSrc());
        }
        if (slider.getThumbImage() != null) {
            imageSlider.setThumbImage(resourcePath + slider.getThumbImage().getSrc());
        }
        if (slider.getMaxTrackImage() != null) {
            imageSlider.setMaxTrackImage(resourcePath + slider.getMaxTrackImage().getSrc());
        }
        if (slider.getMinImage() != null && slider.getMaxImage() != null) {
            addMinAndMaxImage(slider, resourcePath);
        } else {
            add(imageSlider);
        }
    }

    /**
    * Adds the min and max image.
    * If click the min image, the value would be min; and if click the max image, the value would be max. 
    * @param slider the slider
    * @param resourcePath the resource path
    */
    private void addMinAndMaxImage(final Slider slider, String resourcePath) {
        String minImage = resourcePath + slider.getMinImage().getSrc();
        String maxImage = resourcePath + slider.getMaxImage().getSrc();
        imageSlider.setMinImage(minImage);
        imageSlider.setMaxImage(maxImage);
        FlexTable table = new FlexTable();
        table.setCellPadding(0);
        table.setCellSpacing(0);
        table.setPixelSize(slider.getFrameWidth(), slider.getFrameHeight());
        Image min = new Image(minImage);
        min.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                if (!slider.isPassive()) {
                    sendCommand("" + slider.getMinValue());
                    imageSlider.setValue(slider.getMinValue());
                }
            }
        });
        Image max = new Image(maxImage);
        max.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                if (!slider.isPassive()) {
                    sendCommand("" + slider.getMaxValue());
                    imageSlider.setValue(slider.getMaxValue());
                }
            }
        });
        if (slider.isVertical()) {
            imageSlider.setHeight(slider.getFrameHeight() - 40);
            table.setWidget(0, 0, max);
            table.setWidget(1, 0, imageSlider);
            table.setWidget(2, 0, min);
        } else {
            imageSlider.setWidth(slider.getFrameWidth() - 40);
            table.setWidget(0, 0, min);
            table.setWidget(0, 1, imageSlider);
            table.setWidget(0, 2, max);
        }
        add(table);
    }

    public void addPollingSensoryListener() {
        final Integer sensorId = ((Slider) getComponent()).getSensor().getSensorId();
        if (sensorId > 0) {
            ORListenerManager.getInstance().addOREventListener(Constants.ListenerPollingStatusIdFormat + sensorId, new OREventListener() {

                public void handleEvent(OREvent event) {
                    String value = ClientDataBase.statusMap.get(sensorId.toString()).toLowerCase();
                    try {
                        int valueInt = Integer.parseInt(value);
                        imageSlider.setValue(valueInt);
                    } catch (NumberFormatException e) {
                        MessageBox.alert("ERROR", "The returned format of polling value " + value + " for slider is wrong", null);
                        return;
                    }
                }
            });
        }
    }

    public void sendCommand(String value) {
        super.sendCommand(value, new AsyncSuccessCallback<Void>() {

            public void onSuccess(Void result) {
            }
        });
    }
}
