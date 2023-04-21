package educate.sis.library;

public class Loan implements java.io.Serializable {

    private String member_id;

    private String book_id;

    private String item_id;

    private String copy_id;

    private SimpleDate out_date;

    private SimpleDate due_date;

    private SimpleDate in_date;

    private double fine_assessed;

    private double fine_paid;

    private double fine_waived;

    public Loan(String member_id, String book_id, String item_id, String copy_id) {
        this.member_id = member_id;
        this.book_id = book_id;
        this.item_id = item_id;
        this.copy_id = copy_id;
    }

    public void setDate(String out_date, String due_date) {
        this.out_date = new SimpleDate(out_date);
        this.due_date = new SimpleDate(due_date);
    }

    public void setOutDate(String out_date) {
        this.out_date = new SimpleDate(SimpleDate.DMY, out_date);
    }

    public void setInDate(String in_date) {
        this.in_date = new SimpleDate(SimpleDate.DMY, in_date);
    }

    public void setDueDate(String due_date) {
        this.due_date = new SimpleDate(SimpleDate.DMY, due_date);
    }

    public void setOutDate(SimpleDate out_date) {
        this.out_date = out_date;
    }

    public void setInDate(SimpleDate in_date) {
        this.in_date = in_date;
    }

    public void setDueDate(SimpleDate due_date) {
        this.due_date = due_date;
    }

    public void setFineAssessed(double fine_assessed) {
        this.fine_assessed = fine_assessed;
    }

    public void setFinePaid(double fine_paid) {
        this.fine_paid = fine_paid;
    }

    public void setFineWaived(double fine_waived) {
        this.fine_waived = fine_waived;
    }

    public String getMemberId() {
        return member_id;
    }

    public String getBookId() {
        return book_id;
    }

    public String getItemId() {
        return item_id;
    }

    public String getCopyId() {
        return copy_id;
    }

    public SimpleDate getOutDate() {
        return out_date;
    }

    public SimpleDate getDueDate() {
        return due_date;
    }

    public SimpleDate getInDate() {
        return in_date;
    }

    public double getFineAssessed() {
        return fine_assessed;
    }

    public double getFinePaid() {
        return fine_paid;
    }

    public double getFineWaived() {
        return fine_waived;
    }

    public static class DueDateComparator implements java.util.Comparator {

        public int compare(Object o1, Object o2) {
            Loan loan1 = (Loan) o1;
            Loan loan2 = (Loan) o2;
            return loan1.compareByDueDate(loan2);
        }
    }

    public int compareByDueDate(Loan loan2) {
        SimpleDate due_date2 = loan2.getDueDate();
        return due_date.compare(due_date2);
    }

    public static class InDateComparator implements java.util.Comparator {

        public int compare(Object o1, Object o2) {
            Loan loan1 = (Loan) o1;
            Loan loan2 = (Loan) o2;
            return loan1.compareByInDate(loan2);
        }
    }

    public int compareByInDate(Loan loan2) {
        SimpleDate in_date2 = loan2.getInDate();
        return in_date.compare(in_date2);
    }

    public static class InDateDescComparator implements java.util.Comparator {

        public int compare(Object o1, Object o2) {
            Loan loan1 = (Loan) o1;
            Loan loan2 = (Loan) o2;
            return loan2.compareByInDate(loan1);
        }
    }
}
