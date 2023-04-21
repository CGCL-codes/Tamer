package org.zkoss.zkex.wire;

import java.io.IOException;
import org.zkoss.lang.Objects;
import org.zkoss.zk.ui.HtmlBasedComponent;
import org.zkoss.zk.ui.sys.ContentRenderer;

public class Wire extends HtmlBasedComponent {

    private static final long serialVersionUID = 20091211122313L;

    private Wirebox _in;

    private Wirebox _out;

    private String _config;

    private String _joint;

    public Wire() {
    }

    protected void renderProperties(ContentRenderer renderer) throws IOException {
        super.renderProperties(renderer);
        if (getIn() != null) {
            render(renderer, "_in", "uuid(" + getIn().getUuid() + ")");
        }
        if (getOut() != null) {
            render(renderer, "_out", "uuid(" + getOut().getUuid() + ")");
        }
        if (getConfig() != null) {
            render(renderer, "config", getConfig());
        }
        if (_joint != null) {
            render(renderer, "joint", _joint);
        }
    }

    /**
	 * No child is allowed.
	 */
    protected boolean isChildable() {
        return false;
    }

    /**
	 * get the wirebox .
	 * 
	 * @return
	 */
    public Wirebox getIn() {
        if (_in != null) return _in;
        return _in;
    }

    /**
	 * set the wirebox .
	 * 
	 * @return
	 */
    public void setIn(Wirebox in) {
        if (!Objects.equals(_in, in)) {
            if (_in != null) _in.removeIn(this);
            _in = in;
            _in.addIn(this);
        }
    }

    /**
	 * set the ID of wirebox in
	 * 
	 * @param id
	 *            wirebox's id
	 */
    public void setIn(String id) {
        setIn((Wirebox) getFellow(id));
    }

    /**
	 * get the wirebox out
	 * 
	 * @return
	 */
    public Wirebox getOut() {
        if (_out != null) return _out;
        return _out;
    }

    /**
	 * set the wirebox out
	 * 
	 * @param _out
	 */
    public void setOut(Wirebox out) {
        if (!Objects.equals(_out, out)) {
            if (_out != null) _out.removeIn(this);
            _out = out;
            _out.addOut(this);
        }
    }

    /**
	 * set the id of wirebox out
	 * 
	 * @param id
	 *            wirebox's id
	 */
    public void setOut(String id) {
        setOut((Wirebox) getFellow(id));
    }

    /**
	 * we have the configs below (we follow wireit use
	 * "key1=value1,key2=value2,key3=value3" style) <Br />
	 * 
	 * <ul>
	 * <li>
	 * color : the line color , ex. #DDDD22</li>
	 * <li>
	 * drawingMethod: this support 6 types from wireit .
	 * <ul>
	 * <li>
	 * straight</li>
	 * <li>
	 * bezier</li>
	 * <li>
	 * arrow</li>
	 * <li>
	 * bezierArrow</li>
	 * <li>
	 * leftSquareArrow</li>
	 * <li>
	 * rightSquareArrow</li>
	 * </ul>
	 * </li>
	 * <li>
	 * cap : the line cap</li>
	 * <li>
	 * width : the line width (without border)</li>
	 * <li>
	 * borderwidth : the border width</li>
	 * <li>
	 * bordercolor : the border color</li>
	 * <li>
	 * bordercap : the border cap</li>
	 * 
	 * </ul>
	 * 
	 * @return
	 */
    public String getConfig() {
        return _config;
    }

    public void setConfig(String _config) {
        this._config = _config;
    }

    public String getJoint() {
        return _joint;
    }

    /**
	 * if they didnt give joint , we dont show it.
	 * 
	 * @param joint
	 */
    public void setJoint(String joint) {
        this._joint = joint;
    }
}
