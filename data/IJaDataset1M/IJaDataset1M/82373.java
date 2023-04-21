package com.jb.util;

import java.util.ArrayList;

/**
 *
 * @author duo
 */
public class JavaScriptMenuHeader {

    String nome;

    ArrayList<String> submenus;

    ArrayList<String> targets;

    public JavaScriptMenuHeader() {
    }

    public JavaScriptMenuHeader(String nome) {
        setNome(nome);
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void add(String submenu, String target) {
        if (submenus == null) {
            submenus = new ArrayList<String>();
        }
        submenus.add(submenu);
        if (targets == null) {
            targets = new ArrayList<String>();
        }
        targets.add(target);
    }

    public String getSubmenu(int index) {
        return submenus.get(index);
    }

    public String getTarget(int index) {
        if (targets.get(index) == null) {
            return "#";
        }
        return targets.get(index);
    }

    public ArrayList<String> getSubmenus() {
        return submenus;
    }
}
