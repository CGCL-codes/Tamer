package com.azaleait.asterion.customactions.commands;

import javax.servlet.jsp.JspException;

public interface CommandWithParameters {

    void addParameter(final String name, final String value) throws JspException;
}
