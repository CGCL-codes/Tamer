package com.aivik.wordspell.gui;

import javax.swing.JFrame;

/**
 * Test harness for testing and debugging.
 */
public class EditorTest extends JFrame {

    private GTitledTextArea textArea = new GTitledTextArea();

    public EditorTest() {
        super("Word Spell Test");
    }

    public void setUp() {
        textArea.setTitle("Text Area Title");
        textArea.setDisplayRemainingCharacters(true);
        textArea.setEditable(true);
        textArea.setText(TEXT);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.add(textArea);
        this.setSize(500, 400);
        this.setVisible(true);
    }

    /**
     * Main method. For testing and debugging only.
     */
    public static void main(String[] aArgs) {
        EditorTest editorTest = new EditorTest();
        editorTest.setUp();
    }

    private static final String TEXT = "Integer eu est tellus, ac interdum nunc. Praesent massa elit, dignissim quis imperdiet vel, hendrerit vitae massa. Vestibulum tincidunt egestas justo quis tempus. Duis ac mollis ante. Vestibulum pretium porttitor odio sed tristique. Sed adipiscing hendrerit massa ut fringilla. Quisque dapibus nisl purus. Vestibulum accumsan tempor ligula quis mollis. Suspendisse vestibulum, tortor et blandit euismod, lorem orci blandit urna, in ultrices arcu risus eu lectus. Aliquam non risus dolor. Sed sollicitudin, metus sit amet volutpat elementum, dui elit lacinia leo, ac dictum justo urna eget dolor. In congue pharetra odio, vel suscipit dolor tincidunt quis. Proin nec tellus quam. Aliquam vehicula congue nisl interdum convallis. Curabitur ligula metus, ultricies et rhoncus id, malesuada sagittis purus. Phasellus sit amet ipsum nisi. Morbi lacinia dui in leo auctor volutpat. Cum sociis natoque penatibus et magnis dis parturient montes, nascetur ridiculus mus. Phasellus eget sem nec orci pulvinar volutpat. " + "Mauris pharetra risus sit amet augue facilisis id rhoncus lacus blandit. Fusce eu arcu ante. Sed semper, lacus in elementum bibendum, nisl urna mattis leo, eu egestas felis risus congue arcu. Vivamus eu nulla sit amet mauris fringilla iaculis in nec tellus. Quisque vitae pulvinar felis. Suspendisse fermentum ante eu arcu rhoncus interdum. Cras vitae sagittis lorem. Maecenas eget purus in sem accumsan lacinia. Vestibulum ante ipsum primis in faucibus orci luctus et ultrices posuere cubilia Curae; Nulla eget nisl ac velit consequat mollis. Sed nec nisi et tellus laoreet semper. Phasellus sagittis aliquet sollicitudin. Nulla nunc mauris, rhoncus quis varius et, imperdiet non mi. Donec sed mi nisl. Aenean commodo, est eget suscipit posuere, libero sapien sagittis enim, vel sodales dolor quam id velit. Suspendisse quis ornare elit. Vestibulum est mauris, luctus id dignissim eu, posuere et arcu. Sed id mauris nibh, in lacinia augue." + "Ut eu sem orci. Pellentesque ligula purus, adipiscing et scelerisque sodales, consequat in justo. Suspendisse blandit dignissim purus, non ullamcorper sem porttitor in. Morbi eleifend rhoncus arcu, sit amet tristique augue iaculis eget. Suspendisse lacinia arcu vehicula erat venenatis ut congue enim ultrices. Fusce consectetur, neque sit amet sagittis elementum, nisi urna posuere justo, vitae dictum lectus arcu quis lacus. Ut eleifend mi dui, et consectetur nisl. Vivamus tincidunt lacinia rutrum. Donec ac mi sapien. Vestibulum imperdiet, orci at ultricies hendrerit, nunc mauris commodo lacus, quis viverra tortor arcu sit amet libero. Integer vitae quam ante. Nunc placerat ligula fringilla elit condimentum posuere." + "Donec elementum nisi in magna luctus id rutrum dolor blandit. Integer fermentum, mauris eu venenatis tincidunt, nulla turpis volutpat magna, sagittis pulvinar velit nisi nec augue. Nunc tristique cursus ipsum adipiscing pulvinar. Cras id est nisi, eu molestie elit. Pellentesque convallis lacus mauris, vitae rhoncus neque. Aenean sit amet mauris lorem. Morbi faucibus, ligula rutrum dapibus blandit, massa dui commodo nisl, eget tristique neque elit vel ligula. In erat ligula, posuere eu tempus quis, eleifend sit amet ligula. Sed porta dui sit amet dui luctus tristique. Vestibulum ante ipsum primis in faucibus orci luctus et ultrices posuere cubilia Curae; Donec at turpis eu metus pellentesque tempus quis a felis. Nam laoreet ornare eleifend. Aenean vehicula mattis elementum. Nam dapibus risus at leo convallis sed lobortis nibh facilisis. Pellentesque convallis convallis tincidunt. Suspendisse potenti. ";
}
