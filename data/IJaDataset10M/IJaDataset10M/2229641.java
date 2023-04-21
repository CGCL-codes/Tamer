package org.openremote.web.console.panel.entity;

import java.util.List;

public interface GridLayout {

    String getLeft();

    String getTop();

    String getWidth();

    String getHeight();

    Integer getRows();

    Integer getCols();

    List<Cell> getCell();

    void setLeft(Integer left);

    void setTop(Integer top);

    void setWidth(Integer width);

    void setHeight(Integer height);

    void setRows(Integer rows);

    void setCols(Integer cols);

    void setCell(List<Cell> cells);
}
