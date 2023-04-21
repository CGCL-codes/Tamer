package cogitant;

import java.awt.Component;

public class EditorCommandProducer extends CppJava {

    protected Editor m_editor;

    protected Component m_component;

    /** Ce booléen vaut vrai si le producteur a été rajouté à un éditeur, et faux sinon.
	 *	Si le producteur a été rajouté à un éditeur, il ne faut pas détruire l'objet C++ au destructeur de EditorCommandProducer, car l'objet C++ Editor s'en chargera). */
    public boolean m_ineditor = false;

    public EditorCommandProducer(Editor ed, int typecmd, Component component) {
        initCppSide(ed.handlecpp, typecmd, component);
        m_editor = ed;
        m_component = component;
    }

    protected void finalize() {
        deleteCppSide(!m_ineditor);
    }

    public void run() {
        cppRun();
    }

    private native void initCppSide(long editorcpp, int cmd, Component component);

    private native void deleteCppSide(boolean destroy);

    private native void cppRun();
}
