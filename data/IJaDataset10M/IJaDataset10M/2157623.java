package pl.edu.wat.wcy.jit.model;

import pl.edu.wat.wcy.jit.view.bpmn.Element;

public class StopActivity extends BpmnActivity {

    public StopActivity(Element element, int genType, double genParam1, double genParam2) {
        super(element, genType, genParam1, genParam2);
    }

    @Override
    protected void action() {
        Entity e;
        super.action();
        while (this.entityQueue.size() > 0) {
            e = getEntityFromQueue();
            notifyGuiActionStart(e);
            System.out.println("Encja konczy dzialanie. Wielkosc kolejki: " + entityQueue.size());
            this.busy = true;
            this.waitDuration(this.generator.generate(genParam1, genParam2));
            notifyGuiActionStop(e);
        }
        this.busy = false;
    }
}
