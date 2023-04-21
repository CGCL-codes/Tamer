package guiUI;

/**
 * <P>The GuiSwitch class is used to instantiate new Switches on the GUI</P>
 * 
 * @author luke_hamilton
 * @since 15th November 2004
 * @version v0.20
 */
public class GuiSwitch extends DataLinkLayerDevice {

    /**
	 * @param inName  The name of the switch
	 * @param inMainscreen	The JFrame that the Switch will be created on
	 */
    public GuiSwitch(String inName, MainScreen inMainScreen) {
        super(inName, inMainScreen, "images/simulation/switch_large.gif");
    }
}
