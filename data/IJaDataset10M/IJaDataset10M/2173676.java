package org.nakedobjects.plugins.html.component;

public interface DebugPane extends Component {

    void addSection(String title);

    void appendln(String text);

    void indent();

    void unindent();
}
