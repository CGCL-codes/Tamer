package com.javaeedev.test;

import java.io.FileOutputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import com.javaeedev.test.AddressBookProtos.AddressBook;
import com.javaeedev.test.AddressBookProtos.Person;

public class Server {

    public static void main(String[] args) throws Exception {
        Person p1 = buildPerson("test", "test@javaeedev.com", new String[] { "13900000001", "010-10001000", null });
        Person p2 = buildPerson("michael", "michael@javaeedev.com", new String[] { "13900000002", "010-20002000", "010-30003000" });
        Person p3 = buildPerson("bill", "bill@javaeedev.com", new String[] { null, "010-40004000", "010-50005000" });
        AddressBook addressBook = buildAddressBook(p1, p2, p3);
        System.out.println(addressBook.toString());
        if (args.length == 0) {
            OutputStream file = new FileOutputStream("C:\\test.proto.txt");
            addressBook.writeTo(file);
            file.close();
            return;
        }
        ServerSocket ss = new ServerSocket(12345);
        Socket socket = ss.accept();
        OutputStream output = socket.getOutputStream();
        addressBook.writeTo(output);
        output.flush();
        socket.close();
        ss.close();
    }

    static Person buildPerson(String name, String email, String[] phoneNumbers) {
        Person.Builder person = Person.newBuilder();
        person.setName(name);
        person.setEmail(email);
        for (int i = 0; i < 3; i++) {
            String phoneNumber = phoneNumbers[i];
            if (phoneNumber != null) {
                Person.PhoneNumber.Builder phone = Person.PhoneNumber.newBuilder();
                phone.setType(Person.PhoneNumber.Type.valueOf(i));
                phone.setNumber(phoneNumber);
                person.addPhone(phone);
            }
        }
        return person.build();
    }

    static AddressBook buildAddressBook(Person... persons) {
        AddressBook.Builder addressBook = AddressBook.newBuilder();
        for (Person person : persons) addressBook.addPerson(person);
        return addressBook.build();
    }
}
