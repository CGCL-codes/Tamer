package com.antilia.web.dialog;

import org.apache.wicket.Component;
import com.antilia.web.button.IMenuItem;
import com.antilia.web.toolbar.IToolbarItem;

/**
 * @author Ernesto Reinaldo Barreiro (reirn70@gmail.com)
 *
 */
public interface IDialogLink extends IMenuItem, IToolbarItem {

    Component getButton();

    void setDialog(DefaultDialog dialog);
}
