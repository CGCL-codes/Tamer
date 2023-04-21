package com.miranteinfo.seam.hibernate;

import java.io.Serializable;

/**
 * Interface support a todas as entidades.
 * 
 * @author lucas lins
 *
 */
public interface IEntity extends Serializable {

    public Serializable getId();
}
