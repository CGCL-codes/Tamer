package Modell;

import java.util.Vector;
import java.io.IOException;

public abstract class Element {

    Cell cell;

    Game unnamed_Game_;

    skeleton.ConsoleHandler ch = new skeleton.ConsoleHandler();

    public void ReachedBy(Element element) {
        ch.PrintCalled(this);
        ch.PrintReturnOrigin(this, "void");
    }

    public void ReachedBy(Civilian civilian) {
        ch.PrintCalled(this);
        ch.PrintReturnOrigin(this, "void");
    }

    public void ReachedBy(Cop cop) {
        ch.PrintCalled(this);
        ch.PrintReturnOrigin(this, "void");
    }

    public void ReachedBy(Bank bank) {
        ch.PrintCalled(this);
        ch.PrintReturnOrigin(this, "void");
    }

    public void ReachedBy(Lamp lamp) {
        ch.PrintCalled(this);
        ch.PrintReturnOrigin(this, "void");
    }

    public void ReachedBy(Hideout hideout) {
        ch.PrintCalled(this);
        ch.PrintReturnOrigin(this, "void");
    }

    public void ReachedBy(Robber robber) {
        ch.PrintCalled(this);
        ch.PrintReturnOrigin(this, "void");
    }

    public void ReachedBy(Sign sign) {
        ch.PrintCalled(this);
        ch.PrintReturnOrigin(this, "void");
    }

    public void setCell(Cell cell) {
        ch.PrintCalled(this);
        this.cell = cell;
        ch.PrintReturnOrigin(this, "void");
    }

    public Cell getCell() {
        ch.PrintCalled(this);
        ch.PrintReturnOrigin(this, this.cell);
        return this.cell;
    }
}
