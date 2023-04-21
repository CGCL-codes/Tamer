package lablayer.model;

import java.util.*;
import lablayer.controller.Updatable;
import lablayer.model.MathComponents.MathComponent;

/**
 *
 */
class Component extends LLNode implements Componentable {

    private MathComponent mathComponent;

    private Updatable updater;

    public Component() {
        super();
        this.xml = "Add xml here!";
    }

    public Component(MathComponent mathComponent) {
        this();
        try {
            assert mathComponent != null : "constructor Component(MathComponent mathComponent) have a null pointer mathComponent";
            this.mathComponent = mathComponent;
        } catch (Exception e) {
        }
    }

    @Override
    public void calculate() {
        Iterator<LLNode> nodeIterator = this.nodes.iterator();
        try {
            assert mathComponent != null : "mathComponent is null pointer";
            try {
                mathComponent.calculate();
            } catch (Exception e) {
            }
            this.updater.update();
            while (nodeIterator.hasNext()) {
                LLNode testNode = nodeIterator.next();
                if (testNode instanceof Component) {
                    ((Component) testNode).setData(mathComponent.getResult());
                    ((Component) testNode).calculate();
                }
            }
        } catch (Exception e) {
        }
    }

    @Override
    public LabData getData() {
        return mathComponent.getData();
    }

    @Override
    public void setData(LabData data) {
        assert data != null : "Component.setData data is null";
        mathComponent.setData(data);
    }

    @Override
    public String getComponentId() {
        return this.mathComponent.getComponentId();
    }

    @Override
    public String getComponentName() {
        return this.mathComponent.getComponentName();
    }

    public void setMathComponent(MathComponent mathComponent) {
        try {
            assert mathComponent != null : "Component.setMathComponent(MathComponent mathComponent) have a null pointer";
            this.mathComponent = mathComponent;
        } catch (Exception e) {
        }
    }

    public MathComponent getMathComponent() {
        return this.mathComponent;
    }

    @Override
    public LabData getResult() {
        return this.mathComponent.getResult();
    }

    @Override
    public Updatable getUpdater() {
        return updater;
    }

    @Override
    public void setUpdater(Updatable updater) {
        this.updater = updater;
    }

    @Override
    public boolean hasComponent(LLNode node) {
        boolean value = false;
        if (this.nodes.contains(node)) value = true;
        return value;
    }

    @Override
    public void setParameter(String params, String values) {
        this.mathComponent.setParameter(params, values);
    }

    @Override
    public LabData getInitData() {
        return this.mathComponent.getInitData();
    }
}
