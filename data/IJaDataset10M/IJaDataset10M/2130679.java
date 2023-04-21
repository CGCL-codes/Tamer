package br.com.arsmachina.introducaotapestry.components;

import org.apache.tapestry5.annotations.IncludeJavaScriptLibrary;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Property;

/**
 * 
 * @author Thiago H. de Paula Figueiredo
 */
@IncludeJavaScriptLibrary("context:/javascript/util.js")
public class Mostrar {

    @Parameter(required = true, defaultPrefix = "literal")
    @Property
    private String idTag;
}
