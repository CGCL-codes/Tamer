package relationships.factoryUnions;

import date.date.Date;
import person.Person;
import relationships.unions.Divorce;
import relationships.unions.Union;

public class FactoryDivorce extends FactoryMarriage {

    public FactoryDivorce() {
        super();
    }

    @Override
    public Union createUnion(Person spouse1, Person spouse2, Date dateBeginUnion, String beginUnionPlace, Date dateEndUnion, String endUnionPLace) {
        return new Divorce(spouse1, spouse2, dateBeginUnion, beginUnionPlace, dateEndUnion, endUnionPLace);
    }
}
