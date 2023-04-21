package net.sf.brightside.xlibrary.util;

import net.sf.brightside.xlibrary.metamodel.Student;
import net.sf.brightside.xlibrary.service.GetById;
import org.apache.tapestry.ValueEncoder;

public class StudentEncoderImpl implements ValueEncoder<Object>, StudentEncoder {

    private GetById<Student> getByIdCommand;

    @Override
    public String toClient(Object object) {
        String str = "" + ((Student) object).takeId();
        return str;
    }

    public GetById<Student> getGetByIdCommand() {
        return getByIdCommand;
    }

    public void setGetByIdCommand(GetById<Student> getByIdCommand) {
        this.getByIdCommand = getByIdCommand;
    }

    @Override
    public Object toValue(String string) {
        getByIdCommand.setType(Student.class);
        try {
            getByIdCommand.setId(Long.parseLong(string));
            Student student = (Student) getByIdCommand.execute();
            return student;
        } catch (Exception e) {
            return null;
        }
    }
}
