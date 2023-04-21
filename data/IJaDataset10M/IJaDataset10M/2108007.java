package net.sf.mp.demo.conference.domain.conference;

import java.sql.*;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import javax.persistence.metamodel.SetAttribute;
import net.sf.mp.demo.conference.domain.conference.ConferenceMember;
import net.sf.mp.demo.conference.domain.conference.Presentation;

@StaticMetamodel(Evaluation.class)
public class Evaluation_ {

    public static volatile SingularAttribute<Evaluation, Long> id;

    public static volatile SingularAttribute<Evaluation, Integer> star;

    public static volatile SingularAttribute<Evaluation, String> comment;

    public static volatile SingularAttribute<Evaluation, Timestamp> evaluationDate;

    public static volatile SingularAttribute<Evaluation, ConferenceMember> conferenceMemberId;

    public static volatile SingularAttribute<Evaluation, Presentation> presentationId;
}
