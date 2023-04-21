package org.nakedobjects.nos.client.web.component;

public interface Table extends Component {

    void setSummary(String string);

    void addColumnHeader(String name);

    void addRowHeader(Component component);

    void addCell(String string, boolean truncate);

    void addCell(Component component);

    void addEmptyCell();
}
