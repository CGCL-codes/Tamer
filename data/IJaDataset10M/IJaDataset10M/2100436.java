package therandomhomepage.lightboximagedemoclient;

import therandomhomepage.widgets.client.LightboxImage;
import com.google.gwt.user.client.ui.*;

/**
 * Created by IntelliJ IDEA.
 * User: Siddique Hameed
 * Date: Dec 19, 2006
 * Time: 12:10:58 PM
 */
public class SlideshowLightboxWithBackgroundMusicPanel extends Composite {

    private LightboxImage lightboxImage = null;

    private HTML description;

    private Button btnSlideshow;

    public SlideshowLightboxWithBackgroundMusicPanel() {
        VerticalPanel panel = new VerticalPanel();
        panel.setHorizontalAlignment(VerticalPanel.ALIGN_CENTER);
        panel.setVerticalAlignment(VerticalPanel.ALIGN_MIDDLE);
        Label headerLabel = new Label("Slideshow with background music demo");
        panel.add(headerLabel);
        panel.add(new HTML("<br/>"));
        btnSlideshow = new Button("Start Slideshow");
        btnSlideshow.addClickListener(new SlideshowButtonListener());
        panel.add(btnSlideshow);
        description = getDescription();
        description.setVisible(false);
        panel.add(description);
        panel.add(new HTML("<br/>"));
        Image image1 = new Image("image-1.jpg");
        image1.setTitle("Image 1");
        Image image2 = new Image("image-2.jpg");
        image2.setTitle("Image 2");
        Image image3 = new Image("image-3.jpg");
        image3.setTitle("Image 3");
        Image image4 = new Image("image-4.jpg");
        image4.setTitle("Image 4");
        Image image5 = new Image("image-5.jpg");
        image5.setTitle("Image 5");
        Image images[] = { image1, image2, image3, image4, image5 };
        lightboxImage = new LightboxImage(images, true, 3);
        lightboxImage.setSlideshowForever(true);
        lightboxImage.setBackgroundMusicURL("music.mp3");
        lightboxImage.addClickListener(new ClickListener() {

            public void onClick(Widget sender) {
                if (lightboxImage.isSlideshowRunning()) {
                    stopSlideshow();
                }
            }
        });
        panel.add(lightboxImage);
        panel.add(getCodeSnippet());
        initWidget(panel);
    }

    private HTML getDescription() {
        return new HTML("<br/>Image will be changing every 3 seconds. Click on the image to see slideshow with background music.<br/>");
    }

    private HTML getCodeSnippet() {
        return new HTML("<h3>&nbsp;&nbsp;&nbsp;Code Snippet : </h3><pre class=\"code\">        Image image1 = new Image(\"image-1.jpg\");\n" + "        image1.setTitle(\"Image 1\");\n" + "\n" + "        Image image2 = new Image(\"image-2.jpg\");\n" + "        image2.setTitle(\"Image 2\");\n" + "\n" + "        Image image3 = new Image(\"image-3.jpg\");\n" + "        image3.setTitle(\"Image 3\");\n" + "\n" + "        Image image4 = new Image(\"image-4.jpg\");\n" + "        image4.setTitle(\"Image 4\");\n" + "\n" + "        Image image5 = new Image(\"image-5.jpg\");\n" + "        image5.setTitle(\"Image 5\");\n" + "\n" + "        Image images[] = {image1, image2, image3, image4, image5};\n" + "        lightboxImage = new LightboxImage(images,true,3);\n" + "        lightboxImage.setSlideshowForever(true);\n" + "        lightboxImage.setBackgroundMusicURL(\"music.mp3\");\n" + "        lightboxImage.addClickListener(new ClickListener(){\n" + "            public void onClick(Widget sender) {\n" + "                if (lightboxImage.isSlideshowRunning()) {\n" + "                    stopSlideshow();\n" + "                }\n" + "            }\n" + "        });" + "\n\n" + "    private void stopSlideshow() {\n" + "        description.setVisible(false);\n" + "        lightboxImage.stopSlideshow();\n" + "        btnSlideshow.setText(\"Start Slideshow\");\n" + "    }" + "</pre>");
    }

    private void stopSlideshow() {
        description.setVisible(false);
        lightboxImage.stopSlideshow();
        btnSlideshow.setText("Start Slideshow");
    }

    private void startSlideshow() {
        description.setVisible(true);
        lightboxImage.startSlideshow();
        btnSlideshow.setText("Stop Slideshow");
    }

    private class SlideshowButtonListener implements ClickListener {

        public void onClick(Widget sender) {
            if (btnSlideshow.getText().equals("Start Slideshow")) {
                startSlideshow();
            } else {
                stopSlideshow();
            }
        }
    }
}
