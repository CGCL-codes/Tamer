package gui;

import sys.GUITest;
import utils.PManager;
import utils.ReflectUtils;
import utils.video.VideoManager;

public class SetBackgroundTest extends GUITest {

    @org.junit.Test
    @Override
    public void Test() {
        bot.menu("Video").menu("Source").menu("Camera").click();
        bot.button("Start Stream").click();
        sleep(2000);
        System.out.println("setting background..");
        bot.button("Set Background").click();
        sleep(1000);
        int[] bgData = (int[]) ReflectUtils.getField(VideoManager.class, PManager.getDefault().getVideoManager(), "bg_image_rgb");
        assert (bgData[100] != 0) : "Background is Null!";
        sleep(1000);
    }
}
