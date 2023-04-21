package tudresden.ocl20.pivot.ocl2parser.testcasegenerator.gen.testcasegenerator.node;

import java.util.*;
import tudresden.ocl20.pivot.ocl2parser.testcasegenerator.gen.testcasegenerator.analysis.*;

public final class ATestcaseTestpackage extends PTestpackage {

    private PTestcasefile _testcasefile_;

    public ATestcaseTestpackage() {
    }

    public ATestcaseTestpackage(PTestcasefile _testcasefile_) {
        setTestcasefile(_testcasefile_);
    }

    public Object clone() {
        return new ATestcaseTestpackage((PTestcasefile) cloneNode(_testcasefile_));
    }

    public void apply(Switch sw) {
        ((Analysis) sw).caseATestcaseTestpackage(this);
    }

    public Object apply(SwitchWithReturn sw, Object param) throws AttrEvalException {
        return ((AnalysisWithReturn) sw).caseATestcaseTestpackage(this, param);
    }

    public PTestcasefile getTestcasefile() {
        return _testcasefile_;
    }

    public void setTestcasefile(PTestcasefile node) {
        if (_testcasefile_ != null) {
            _testcasefile_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        _testcasefile_ = node;
    }

    public String toString() {
        return "" + toString(_testcasefile_);
    }

    void removeChild(Node child) {
        if (_testcasefile_ == child) {
            _testcasefile_ = null;
            return;
        }
    }

    void replaceChild(Node oldChild, Node newChild) {
        if (_testcasefile_ == oldChild) {
            setTestcasefile((PTestcasefile) newChild);
            return;
        }
    }
}
