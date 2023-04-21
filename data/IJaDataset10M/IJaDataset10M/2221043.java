package stack.era.constants;

public class CrimeAttributes {

    public static enum CrimeType {

        MURDER("Murder", 50), MINOR_THEFT("Minor Theft", 30), MAJOR_THEFT("Major Theft", 20);

        private String fullName;

        private int infamy;

        private CrimeType(String fullName, int infamy) {
            this.fullName = fullName;
            this.infamy = infamy;
        }

        public String getFullName() {
            return fullName;
        }

        public int getInfamy() {
            return infamy;
        }
    }
}
