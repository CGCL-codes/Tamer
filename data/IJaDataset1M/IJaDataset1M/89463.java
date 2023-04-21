package main.java.org.opensih.action;

import javax.ejb.Local;

@Local
public interface ModeloSeam {

    public abstract String boton();

    public String getMens();

    public void setMens(String mens);
}
