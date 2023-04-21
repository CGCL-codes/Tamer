package orm;

import java.util.List;
import org.hibernate.criterion.DetachedCriteria;
import org.orm.PersistentSession;
import org.orm.criteria.*;

public class SA_AreaDetachedCriteria extends AbstractORMDetachedCriteria {

    public final StringExpression id_area;

    public final StringExpression descripcion;

    public SA_AreaDetachedCriteria() {
        super(orm.SA_Area.class, orm.SA_AreaCriteria.class);
        id_area = new StringExpression("id_area", this.getDetachedCriteria());
        descripcion = new StringExpression("descripcion", this.getDetachedCriteria());
    }

    public SA_AreaDetachedCriteria(DetachedCriteria aDetachedCriteria) {
        super(aDetachedCriteria, orm.SA_AreaCriteria.class);
        id_area = new StringExpression("id_area", this.getDetachedCriteria());
        descripcion = new StringExpression("descripcion", this.getDetachedCriteria());
    }

    public SA_DepartamentoDetachedCriteria createArea_id_deptoCriteria() {
        return new SA_DepartamentoDetachedCriteria(createCriteria("area_id_depto"));
    }

    public SA_Area uniqueSA_Area(PersistentSession session) {
        return (SA_Area) super.createExecutableCriteria(session).uniqueResult();
    }

    public SA_Area[] listSA_Area(PersistentSession session) {
        List list = super.createExecutableCriteria(session).list();
        return (SA_Area[]) list.toArray(new SA_Area[list.size()]);
    }
}
