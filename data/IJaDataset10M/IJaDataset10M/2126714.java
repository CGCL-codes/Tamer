package net.sf.hdkp.settings;

public interface SettingsListener {

    public void launchWowChanged(boolean newEnabled);

    public void customWowSettingsChanged(boolean newEnabled);

    public void wowInstallPathChanged(String newInstallPath);

    public void wowGamePathChanged(String newGamePath);
}
