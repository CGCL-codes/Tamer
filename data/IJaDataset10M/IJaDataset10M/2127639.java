package org.net.p2p;

import java.io.*;

/**
 *
 * @author subodh
 * Copyright 2008 Subodh Iyengar
 *  This file is part of jnmp2p.

    jnmp2p is free software; you can redistribute it and/or modify
    it under the terms of the Lesser GNU General Public License as published by
    the Free Software Foundation; either version 3 of the License, or
    (at your option) any later version.

    jnmp2p is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    Lesser GNU General Public License for more details.

    You should have received a copy of the Lesser GNU General Public License
    along with jnmp2p; if not, write to the Free Software
    Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */
public class Msg implements Serializable {

    private String header;

    private Object content;

    private String type;

    private boolean seal = false;

    private String password;

    private boolean passset = false;

    Msg(String h) {
        header = h;
    }

    Msg(String h, Object c) {
        header = h;
        content = c;
        type = c.getClass().getName();
    }

    public Object getContent() {
        return content;
    }

    public String getHeader() {
        return header;
    }

    public void fill(Object c) {
        if (!seal) {
            content = c;
            type = c.getClass().getName();
        }
    }

    public void seal() {
        seal = true;
    }

    public void setPass(String pass) {
        password = pass;
        passset = true;
    }

    public void unseal(String pass) {
        if (pass.equals(password)) {
            seal = false;
        }
    }

    public void unseal() {
        if (!passset) {
            seal = false;
        }
    }
}
