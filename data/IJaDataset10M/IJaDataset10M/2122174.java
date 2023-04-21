package ddss.loader;

import java.util.Date;
import java.util.Hashtable;
import org.ksoap2.serialization.MarshalDate;
import org.ksoap2.serialization.PropertyInfo;

public class TaskPersonnal extends TaskPersonnalBase {

    public static Class TASK_PERSONAL_CLASS = new TaskPersonnal().getClass();

    public int ID;

    public int IdTask;

    public int IdPerson;

    public Date UpdateDate;

    public TaskPersonnal(int iD, int idTask, int idPerson, Date UpdateDate) {
        super();
        this.ID = iD;
        this.IdTask = idTask;
        this.IdPerson = idPerson;
        this.UpdateDate = UpdateDate;
    }

    public TaskPersonnal(int iD, int idTask, int idPerson) {
        super();
        this.ID = iD;
        this.IdTask = idTask;
        this.IdPerson = idPerson;
    }

    public TaskPersonnal() {
    }

    public Object getProperty(int index) {
        switch(index) {
            case 0:
                return ID;
            case 1:
                return IdTask;
            case 2:
                return IdPerson;
            case 3:
                return UpdateDate;
            default:
                return null;
        }
    }

    public int getPropertyCount() {
        return 4;
    }

    public void getPropertyInfo(int index, Hashtable properties, PropertyInfo info) {
        switch(index) {
            case 0:
                info.type = PropertyInfo.INTEGER_CLASS;
                info.name = "ID";
                break;
            case 1:
                info.type = PropertyInfo.INTEGER_CLASS;
                info.name = "IdTask";
                break;
            case 2:
                info.type = PropertyInfo.INTEGER_CLASS;
                info.name = "IdPerson";
                break;
            case 3:
                info.type = MarshalDate.DATE_CLASS;
                info.name = "UpdateDate";
                break;
            default:
                break;
        }
    }

    public void setProperty(int index, Object value) {
        switch(index) {
            case 0:
                ID = Integer.parseInt(value.toString());
                break;
            case 1:
                IdTask = Integer.parseInt(value.toString());
                break;
            case 2:
                IdPerson = Integer.parseInt(value.toString());
                break;
            case 3:
                UpdateDate = (Date) value;
                break;
            default:
                break;
        }
    }
}
