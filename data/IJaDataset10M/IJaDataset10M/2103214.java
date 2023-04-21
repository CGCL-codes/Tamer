package cx.ath.contribs.attributedTree.xml.generalApplication.ui;

import cx.ath.contribs.attributedTree.xml.transform.CategoryBase;
import cx.ath.contribs.attributedTree.xml.transform.TransformEnvironment;

public abstract class GeneralUi<E extends TransformEnvironment> extends CategoryBase<E> {

    public GeneralUi(E env) {
        super(env);
    }
}
