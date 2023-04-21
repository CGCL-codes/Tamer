package Sale.Display;

import Sale.SalesPoint;
import java.awt.*;

/**
  * A DisplayManager supporting AWT controls.
  *
  * @see AWTFormSheet
  * @see AWTMenuSheet
  * @see DisplayManager
  * @see SalesPoint#createDisplayManager
  *
  * @author Steffen Zschaler
  * @version 0.5
  */
public class AWTDisplayManager extends DisplayManager {

    private Point ptFormSheetPos = new Point(5, 5);

    private Point ptMenuSheetPos = new Point(5, 5);

    /**
    * Create a new AWTDisplayManager working with the given SalesPoint.
    *
    * @param theSalesPoint the SalesPoint this DisplayManager is going to work with.
    */
    public AWTDisplayManager(SalesPoint theSalesPoint) {
        super(theSalesPoint);
    }

    /**
    * Create a new AWTDisplayManager working with the given SalesPoint.
    *
    * @param theSalesPoint the SalesPoint this DisplayManager is going to work with.
    * @param ptFormSheetPos the initial location for FormSheets, in screen coordinates.
    * @param ptMenuSheetPos the initial location for MenuSheets, in screen coordinates.
    */
    public AWTDisplayManager(SalesPoint theSalesPoint, Point ptFormSheetPos, Point ptMenuSheetPos) {
        this(theSalesPoint);
        this.ptFormSheetPos = ptFormSheetPos;
        this.ptMenuSheetPos = ptMenuSheetPos;
    }

    /**
    * Create a new platform dependent MenuSheet.
    *
    * <p>Returns a new AWTMenuSheet.</p>
    *
    * @return a new platform dependent MenuSheet.
    *
    * @see AWTMenuSheet
    */
    public MenuSheet createMenuSheet() {
        return new AWTMenuSheet(this, ptMenuSheetPos);
    }

    /**
    * Create a new platform dependent FormSheet.
    *
    * <p>Returns a new AWTFormSheet.</p>
    *
    * @return a new platform dependent FormSheet.
    *
    * @see AWTFormSheet
    */
    public FormSheet createFormSheet() {
        return new AWTFormSheet(ptFormSheetPos);
    }

    /**
    * Returns a short description of this DisplayManager.
    *
    * @return a short description of this DisplayManager.
    */
    public String toString() {
        return "AWT DisplayManager";
    }
}
