package net.nourdine.jp.build;

import java.util.List;

public class JsFileSet extends Buildable {

    private List<String> files;

    public JsFileSet(List<String> files) {
        this.files = files;
    }

    @Override
    public String build(boolean shallIMinify) {
        StringBuilder sb = new StringBuilder();
        for (String file : files) {
            sb.append(new JsFile(file).build(shallIMinify));
        }
        return sb.toString();
    }
}
