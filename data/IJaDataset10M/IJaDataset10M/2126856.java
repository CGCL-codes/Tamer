package Transaction;

import Payroll.Employee;

public abstract class ChangeEmployeeTransaction implements Transaction {

    private int itsEmpid;

    public ChangeEmployeeTransaction(int empid) {
        itsEmpid = empid;
    }

    public void Execute() {
        Employee e = PayrollDatabase.PayrollDatabase.Default().GetEmployee(itsEmpid);
        if (e != null) Change(e); else try {
            throw new Exception(PayrollExceptionMessage.EmployeeNotExist);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
    }

    public abstract void Change(Employee e);
}
