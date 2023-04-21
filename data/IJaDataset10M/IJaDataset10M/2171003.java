package org.ladybug.core.db.filebased;

import org.ladybug.core.db.bytestreamers.MyByteArrayInputStream;
import org.ladybug.core.users.User;

/**
 * @author Aurelian Pop
 */
class UserRecord extends AbstractRecord<User> {

    public UserRecord(final Schema schema) {
        super(schema);
    }

    @Override
    public synchronized byte[] getSerializedData() {
        final User user = getData();
        return getSchema().buildRecord(String.valueOf(getRecordId()), user.getFirstName(), user.getLastName(), user.getLogin(), user.getPassword());
    }

    @Override
    public synchronized void setSerializedData(final byte[] data) {
        final MyByteArrayInputStream myBais = new MyByteArrayInputStream(data);
        final Schema schema = getSchema();
        int fieldNo = 1;
        final long kid = myBais.readLong();
        final String firstName = myBais.readString(schema.getField(fieldNo++).getLength());
        final String lastName = myBais.readString(schema.getField(fieldNo++).getLength());
        final String login = myBais.readString(schema.getField(fieldNo++).getLength());
        final String password = myBais.readString(schema.getField(fieldNo++).getLength());
        final User namedItem = new User(login, firstName, lastName, password);
        setRecordId(kid);
        setData(namedItem);
    }
}
