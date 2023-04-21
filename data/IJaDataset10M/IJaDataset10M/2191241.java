package com.novocode.naf.model;

/**
 * A model that contains a single int value that can be modified.
 * 
 * @author Stefan Zeiger (szeiger@novocode.com)
 * @since Apr 22, 2004
 */
public interface IIntModifyModel extends IModel {

    public void setInt(int i);
}
