package uips.modelsManagement;

import java.util.Map;

public interface IClientInfo {

    /**
     * Class id of interfaces which will be displayed as about interface
     *
     * @return Class id of interfaces which will be displayed as about interface
     */
    public String getAboutInterfaceClass();

    /**
     * URI of application icon
     *
     * @return URI of application icon
     */
    public String getApplicationIcon();

    /**
     * Returns true if attention is requied by application
     *
     * @return true if attention is requied by application
     */
    public boolean getAttention();

    /**
     * Class id of interfaces which will be displayed as avatar interface
     *
     * @return Class id of interfaces which will be displayed as avatar interface
     */
    public String getAvatarInterfaceClass();

    /**
     * Class id of interfaces which will be displayed as global interface
     *
     * @return Class id of interfaces which will be displayed as global interface
     */
    public String getGlobalInterfaceClass();

    /**
     * Class id of interfaces which will be displayed as menu interface
     *
     * @return Class id of interfaces which will be displayed as menu interface
     */
    public String getMenuInterfaceClass();

    /**
     * Class id of interfaces which will be displayed as preferences interface
     *
     * @return Class id of interfaces which will be displayed as preferences interface
     */
    public String getPreferencesInterfaceClass();

    /**
     * Returns URI of tray icon
     *
     * @return URI of tray icon
     */
    public String getTrayIcon();

    /**
     * Class id of interfaces which will be displayed as tray menu interface
     *
     * @return Class id of interfaces which will be displayed as tray menu interface
     */
    public String getTrayMenuInterfaceClass();

    /**
     * Returns animations suported by client
     *
     * @return animations suported by client
     */
    public String getAnimations();

    /**
     * Class (id) of UIProtocol client
     *
     * @return Class (id) of UIProtocol client
     */
    public String getClientClass();

    /**
     * Returns containers supported by client
     *
     * @return containers supported by client
     */
    public String getContainers();

    /**
     * Returns elements supported by client
     *
     * @return elements supported by client
     */
    public String getElements();

    /**
     * Set of interfaces that is client able to render
     *
     * @return Set of interfaces that is client able to render
     */
    public String getInterfaces();

    /**
     * Returns interpolators supported by client
     *
     * @return interpolators supported by client
     */
    public String getInterpolators();

    /**
     * Prefered language of user using this client device
     *
     * @return Prefered language of user using this client device
     */
    public String getLanguage();

    /**
     * Set of layouts supproted by client
     *
     * @return Set of layouts supproted by client
     */
    public String getLayouts();

    /**
     * Map with other properties sent from client in connection event
     * (key - property name, value - property value)
     *
     * @return Map with other properties sent from client in connection event
     * (key - property name, value - property value)
     */
    public Map<String, String> getProperties();

    /**
     * Class of root interface (usually navigation IF)
     *
     * @return Class of root interface (usually navigation IF)
     */
    public String getRootInterfaceClass();

    /**
     * Current screen height of client device in pixels
     *
     * @return Current screen height of client device in pixels
     */
    public int getScreen_height();

    /**
     * Current screen width of client device in pixels
     *
     * @return Current screen width of client device in pixels
     */
    public int getScreen_width();

    /**
     * Name of user currently using client device
     *
     * @return Name of user currently using client device
     */
    public String getUserName();

    /**
     * Passowrd of user currently using client device
     *
     * @return Passowrd of user currently using client device
     */
    public String getUserPassword();

    /**
     * Current screen height of client device in pixels
     *
     * @param screen_height Current screen height of client device in pixels
     */
    public void setScreen_height(int screen_height);

    /**
     * Current screen width of client device in pixels
     *
     * @param screen_width Current screen width of client device in pixels
     */
    public void setScreen_width(int screen_width);

    /**
     * Name of user currently using client device
     *
     * @param userName Name of user currently using client device
     */
    public void setUserName(String userName);

    /**
     * Passowrd of user currently using client device
     *
     * @param userPassword Passowrd of user currently using client device
     */
    public void setUserPassword(String userPassword);

    /**
     * Sets containers supported by client
     *
     * @param containers containers supported by client
     */
    public void setContainers(String containers);

    /**
     * Sets elements supported by client
     *
     * @param elements elements supported by client
     */
    public void setElements(String elements);

    /**
     * Set of interfaces that is client able to render
     *
     * @param interfaces interfaces that is client able to render
     */
    public void setInterfaces(String interfaces);

    /**
     * Set of layouts supproted by client
     *
     * @param layouts layouts supproted by client
     */
    public void setLayouts(String layouts);

    /**
     * Sets animations suported by client
     *
     * @param animations animations suported by client
     */
    public void setAnimations(String animations);

    /**
     * Sets interpolators supported by client
     *
     * @param interpolators interpolators supported by client
     */
    public void setInterpolators(String interpolators);

    /**
     * Adds other property sent from client in connection event
     * (key - property name, value - property value)
     *
     * @param key property name
     * @param value property value
     */
    public void setProperty(String key, String value);
}
