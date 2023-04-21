package org.opensih.gdq.Actions;

public interface IBuscarPaciente {

    public void inicio();

    public String buscar();

    public void seteo();

    public abstract void destroy();

    public String getValor();

    public void setValor(String valor);

    public String getCi();

    public void setCi(String ci);

    public boolean isHpg2E();

    public void setHpg2E(boolean hpg2E);

    public String getGuion();

    public void setGuion(String guion);

    public String getInfoContacto();

    public void setInfoContacto(String infoContacto);

    public boolean isDisContacto();

    public void setDisContacto(boolean disContacto);
}
