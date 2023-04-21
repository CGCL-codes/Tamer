package transmissions.responses;

import java.math.BigInteger;
import java.util.UUID;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "lesson_list_response")
public class LessonListResponse extends Response {

    @XmlElement(required = true, name = "lesson_id")
    protected BigInteger[] lessonIDs;

    protected LessonListResponse() {
    }

    public LessonListResponse(UUID uuid, BigInteger[] bigIntegers) {
        super(uuid);
        this.lessonIDs = bigIntegers;
    }

    public BigInteger[] getLessonIDs() {
        return lessonIDs;
    }
}
