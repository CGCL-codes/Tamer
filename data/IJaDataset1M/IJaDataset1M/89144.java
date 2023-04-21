package org.jboss.tutorial.ssl.bean;

import javax.ejb.Remote;

@Remote
public interface Calculator {

    int add(int x, int y);

    int subtract(int x, int y);

    int divide(int x, int y);
}
