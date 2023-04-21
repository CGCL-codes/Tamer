package moten.david.xuml.model.example.microwave;

import moten.david.xuml.model.viewer.ViewerUtil;

public class MicrowaveViewer {

    public static void main(String[] args) throws Exception {
        ViewerUtil.view("src/viewer", Microwave.class);
    }
}
