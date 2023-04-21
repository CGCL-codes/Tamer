package com.jb.util;

import java.util.ArrayList;

/**
 *
 * @author duo
 */
public class JavaScriptMenu {

    String nome;

    ArrayList<JavaScriptMenuHeader> headers;

    public JavaScriptMenu() {
    }

    public JavaScriptMenu(String nome) {
        setNome(nome);
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public JavaScriptMenuHeader addHeader(String nome) {
        JavaScriptMenuHeader header = new JavaScriptMenuHeader(nome);
        if (headers == null) {
            headers = new ArrayList<JavaScriptMenuHeader>();
        }
        headers.add(header);
        return header;
    }

    public ArrayList<JavaScriptMenuHeader> getHeaders() {
        return headers;
    }
}
