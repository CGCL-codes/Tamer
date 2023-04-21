package org.shapelogic.imageutil;

import org.shapelogic.util.BeanUtilsParser;
import org.shapelogic.util.KeyValueParser;
import static org.shapelogic.imageutil.ImageJConstants.*;

/** Class to subclass when writing filters.<br />
 * 
 * @author Sami Badawi
 *
 */
public abstract class BaseImageOperation implements ImageOperation {

    protected SLImage _image;

    protected GuiWrapper _guiWrapper;

    /** This is an argument with information about what should be run. */
    protected String _arg;

    protected int _setupReturnValue;

    protected KeyValueParser _keyValueParser;

    public BaseImageOperation(int setupReturnValue, String arg, SLImage imp) {
        _setupReturnValue = setupReturnValue;
        _image = imp;
        try {
            _keyValueParser = new BeanUtilsParser();
        } catch (Exception ex) {
            System.out.println("Could not create a BeanUtilsParser. " + ex.getMessage());
            ex.printStackTrace();
        }
        setArg(arg);
    }

    public BaseImageOperation(int setupReturnValue, SLImage imp) {
        this(setupReturnValue, null, imp);
    }

    public BaseImageOperation(SLImage imp) {
        this(DOES_ALL, null, imp);
    }

    public BaseImageOperation(int setupReturnValue) {
        this(setupReturnValue, null, null);
    }

    public BaseImageOperation() {
        this(DOES_ALL);
    }

    @Override
    public SLImage getImage() {
        return _image;
    }

    /** If you are using the ImageOperation to fit into a ImageJ PlugInFilter
	 * use this to instantiate arg and image.<br />
	 */
    @Override
    public int setup(String arg, SLImage image) {
        setArg(arg);
        _image = image;
        if (arg.equals("about")) {
            showAbout();
            return DONE;
        } else if (image == null) showAbout();
        return _setupReturnValue;
    }

    @Override
    public void showMessage(String title, String text) {
        if (_guiWrapper != null) _guiWrapper.showMessage(title, text); else System.out.println(title + "\n" + text + "\n");
    }

    @Override
    public void setGuiWrapper(GuiWrapper guiWrapper) {
        _guiWrapper = guiWrapper;
    }

    @Override
    public GuiWrapper getGuiWrapper() {
        return _guiWrapper;
    }

    /** Filter capability is encoded in _setupReturnValue compare it to the type input image.<br /> 
	 * 
	 * XXX This is not fully implemented yet.
	 * */
    @Override
    public boolean isImageValid() {
        boolean result = false;
        if ((_setupReturnValue & DOES_8G) != 0 && _image.isGray()) result = true; else if ((_setupReturnValue & DOES_RGB) != 0 && _image.isRgb()) result = true; else if ((_setupReturnValue & DOES_16) != 0 && _image.getNChannels() == 2) result = true;
        return result;
    }

    @Override
    public int getSetupReturnValue() {
        return _setupReturnValue;
    }

    @Override
    public void setSetupReturnValue(int setupReturnValue) {
        _setupReturnValue = setupReturnValue;
    }

    @Override
    public String showAbout() {
        String message = "";
        if ((_setupReturnValue & DOES_8G) != 0) message += "Works for 8 bit Gray\n";
        if ((_setupReturnValue & DOES_8C) != 0) message += "Works for 8 bit Index\n";
        if ((_setupReturnValue & DOES_16) != 0) message += "Works for 16 bit Cray\n";
        if ((_setupReturnValue & DOES_RGB) != 0) message += "Works for 32 bit Color\n";
        showMessage("About " + getClass().getSimpleName(), message);
        return message;
    }

    @Override
    public KeyValueParser getKeyValueParser() {
        return _keyValueParser;
    }

    @Override
    public void setKeyValueParser(KeyValueParser keyValueParser) {
        _keyValueParser = keyValueParser;
    }

    @Override
    public void setArg(String arg) {
        if (arg == null) return;
        _arg = arg;
        if (_keyValueParser == null) return;
        _keyValueParser.parse(this, _arg);
    }
}
