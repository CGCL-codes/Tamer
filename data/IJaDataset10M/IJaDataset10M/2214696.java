package com._pmz0178.blogtxt.swing.menu;

/**
 * Standart submenu for entity 
 * @author pasha
 *
 * @date 20080302
 */
public interface IEntityMenuCommand {

    void create();

    boolean isDeletionAllowed();

    boolean isEditAllowed();

    void delete();

    void edit();
}
