package org.brandao.jcptbr;

/**
 *
 * @author Cliente
 */
public interface EntityManager {

    public Configuration getConfiguration();

    public JcptbrSession getSession();

    public boolean isOpen();

    public void close();
}
