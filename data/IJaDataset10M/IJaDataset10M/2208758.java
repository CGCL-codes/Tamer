package de.dhbwmannheim.tim.scp.forms;

import java.io.File;
import java.util.Random;
import de.dhbwmannheim.tim.ap.Controller;

public class MyCont extends Controller {

    @Override
    public String speechInput(String textInput) {
        System.out.println(textInput);
        String path = "src/main/resources/emotions/";
        File dir = new File(path);
        File[] videos = dir.listFiles();
        return videos[new Random().nextInt(videos.length)].getPath();
    }
}
