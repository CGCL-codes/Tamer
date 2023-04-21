package crypto.rsacomp.client;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import crypto.rsacomp.common.Employee;
import crypto.rsacomp.common.Print;
import crypto.rsacomp.common.Util;

public class ClientTest extends Client {

    public static void main(String[] args) throws Exception {
        Util.addJsafeJCE();
        new AliceTest().aliceTest();
    }

    static class AliceTest extends Client {

        private void aliceTest() throws Exception {
            Print.setPefix("CLIENT");
            init("client-keystore.jks");
            Print.println();
            Print.println("======== Creating Employee Fred ========");
            Employee fred1 = new Employee("123", "Fred", "Sales");
            setField(fred1, Employee.SALARY, "123.45");
            cloudStub.putEmployee(fred1);
            Print.println();
            Print.println("======== Retrieving Employee Fred ========");
            Employee fred2 = cloudStub.findEmployee("123");
            String encryptedSalary = fred2.getField(Employee.SALARY);
            Print.println("======== Raw, Encrypted Salary " + encryptedSalary);
            String salary = getField(fred2, Employee.SALARY);
            Print.println("======== Decrypted Salary " + salary);
            assertEquals("123.45", salary);
            assertEquals(encryptedSalary, "D64B14D183541518EACE3D5E053F50A8");
            Print.println();
            Print.println("======== Creating Employee Joe ========");
            Employee joe = new Employee("456", "Joe", "Engineering");
            setField(joe, Employee.SALARY, "234.56");
            cloudStub.putEmployee(joe);
            Print.println();
            Print.println("========= The following keys are all different, so Fred's key cannot be used to access Joe's salary.");
            SecretKey joeSalFek = policyStub.getFieldEncryptionKey(joe, Employee.SALARY);
            Print.println("Joe Salary: " + Print.toHexString(joeSalFek.getEncoded()));
            SecretKey joeHealthFek = policyStub.getFieldEncryptionKey(joe, Employee.HEALTH);
            Print.println("Joe Health: " + Print.toHexString(joeHealthFek.getEncoded()));
            SecretKey fredSalFek = policyStub.getFieldEncryptionKey(fred1, Employee.SALARY);
            Print.println("Fred Salary: " + Print.toHexString(fredSalFek.getEncoded()));
            SecretKey fredKey = policyStub.getFieldEncryptionKey(fred1, fred1.SALARY);
            String joeEncryptedSalary = joe.getField(joe.SALARY);
            Print.println("=========  Joe's encrypted Salary: " + joeEncryptedSalary);
            Cipher aesDecrypter = Cipher.getInstance("AES/CBC/NoPadding", "JsafeJCE");
            aesDecrypter.init(Cipher.DECRYPT_MODE, fredKey, ivParamSpec);
            byte[] ciphertext = Print.hexStringToByteArray(joeEncryptedSalary);
            byte[] plaintext = aesDecrypter.doFinal(ciphertext);
            String joesBadSalary = Print.toHexString(plaintext);
            Print.println("======== Failed attempt to access Joe's Salary with Fred's key: " + joesBadSalary);
            assertEquals("D33840DAB1614A9F32A88D6BBF483626", joesBadSalary);
            Print.println("Client ended successfully.");
        }
    }

    static void assertEquals(Object expected, Object actual) {
        if (!expected.equals(actual)) throw new RuntimeException("Assert Equals failed, " + expected + " != " + actual);
    }
}
