package src.projects.SNP_Database.objects;

public class Library {

    private int library_id;

    private String name;

    private String reference;

    private String seq_type;

    private int read_length;

    private int read_length_alt;

    private int platform_id;

    private boolean cancer;

    private String cancer_type;

    private String sample_organ;

    private String sample_notes;

    private String pubmed_id;

    private String file_location;

    private String contact;

    private String protocol;

    private String sample_cell_type;

    public Library(int library_id, String name, String reference, String seq_type, int read_length, int read_length_alt, boolean cancer, String cancer_type, String sample_organ, String sample_notes, String pubmed_id, String file_location, String contact, String protocol, String sample_cell_type) {
        this.set_Library_id(library_id);
        this.set_Name(name);
        this.set_reference(reference);
        this.set_Seq_type(seq_type);
        this.set_Read_length(read_length);
        this.set_Read_length_alt(read_length_alt);
        this.set_cancer(cancer);
        this.set_cancer_type(cancer_type);
        this.set_Sample_organ(sample_organ);
        this.set_Sample_notes(sample_notes);
        this.set_pubmed_id(pubmed_id);
        this.set_file_location(file_location);
        this.set_contact(contact);
        this.set_protocol(protocol);
        this.set_Sample_cell_type(sample_cell_type);
    }

    /**
	 * Use this function when there is no assigned library name. Library id will
	 * be set to -1, which will insert and auto assign a library name
	 * 
	 * @param name
	 * @param reference
	 * @param seq_type
	 * @param read_length
	 * @param read_length_alt
	 * @param cancer
	 * @param cancer_type
	 * @param sample_organ
	 * @param sample_notes
	 * @param pubmed_id
	 * @param file_location
	 * @param contact
	 * @param protocol
	 * @param sample_cell_type
	 * @param platform_id
	 */
    public Library(String name, String reference, String seq_type, int read_length, int read_length_alt, boolean cancer, String cancer_type, String sample_organ, String sample_notes, String pubmed_id, String file_location, String contact, String protocol, String sample_cell_type, int platform_id) {
        this.set_Library_id(-1);
        this.set_Name(name);
        this.set_reference(reference);
        this.set_Seq_type(seq_type);
        this.set_Read_length(read_length);
        this.set_Read_length_alt(read_length_alt);
        this.set_cancer(cancer);
        this.set_cancer_type(cancer_type);
        this.set_Sample_organ(sample_organ);
        this.set_Sample_notes(sample_notes);
        this.set_pubmed_id(pubmed_id);
        this.set_file_location(file_location);
        this.set_contact(contact);
        this.set_protocol(protocol);
        this.set_Sample_cell_type(sample_cell_type);
        this.set_platform_id(platform_id);
    }

    public void set_Library_id(int library_id) {
        this.library_id = library_id;
    }

    public void set_Name(String name) {
        this.name = name;
    }

    public void set_reference(String reference) {
        this.reference = reference;
    }

    public void set_Seq_type(String seq_type) {
        this.seq_type = seq_type;
    }

    public void set_Read_length(int read_length) {
        this.read_length = read_length;
    }

    public void set_Read_length_alt(int read_length_alt) {
        this.read_length_alt = read_length_alt;
    }

    public void set_platform_id(int platform_id) {
        this.platform_id = platform_id;
    }

    public void set_cancer(boolean cancer) {
        this.cancer = cancer;
    }

    public void set_cancer_type(String cancer_type) {
        this.cancer_type = cancer_type;
    }

    public void set_Sample_organ(String sample_organ) {
        this.sample_organ = sample_organ;
    }

    public void set_Sample_notes(String sample_notes) {
        this.sample_notes = sample_notes;
    }

    public void set_pubmed_id(String pubmed_id) {
        this.pubmed_id = pubmed_id;
    }

    public void set_file_location(String file_location) {
        this.file_location = file_location;
    }

    public void set_contact(String contact) {
        this.contact = contact;
    }

    public void set_protocol(String protocol) {
        this.protocol = protocol;
    }

    public void set_Sample_cell_type(String sample_cell_type) {
        this.sample_cell_type = sample_cell_type;
    }

    public int get_Library_id() {
        return library_id;
    }

    public String get_Name() {
        return name;
    }

    public String get_Reference() {
        return reference;
    }

    public String get_Seq_type() {
        return seq_type;
    }

    public int get_Read_length() {
        return read_length;
    }

    public int get_Read_length_alt() {
        return read_length_alt;
    }

    public int get_Platform_ID() {
        return platform_id;
    }

    public boolean get_Cancer() {
        return cancer;
    }

    public String get_cancer_type() {
        return cancer_type;
    }

    public String get_Sample_organ() {
        return sample_organ;
    }

    public String get_Sample_notes() {
        return sample_notes;
    }

    public String get_pubmed_id() {
        return pubmed_id;
    }

    public String get_file_location() {
        return file_location;
    }

    public String get_contact() {
        return contact;
    }

    public String get_protocol() {
        return protocol;
    }

    public String get_Sample_cell_type() {
        return sample_cell_type;
    }

    public String insert_string_new() {
        if (library_id == -1) {
            return "Insert into Library ( name, reference, seq_type, read_length, " + "read_length_alt, cancer, cancer_type, sample_organ, " + "sample_notes, pubmed_id, file_location, contact, protocol, sample_cell_type, platform_id) VALUES ('" + get_Name() + "', '" + get_Reference() + "', '" + get_Seq_type() + "', '" + get_Read_length() + "', '" + get_Read_length_alt() + "', '" + get_Cancer() + "', '" + get_cancer_type() + "', '" + get_Sample_organ() + "', '" + get_Sample_notes() + "', '" + get_pubmed_id() + "', '" + get_file_location() + "', '" + get_contact() + "', '" + get_protocol() + "', '" + get_Sample_cell_type() + "', '" + get_Platform_ID() + "')";
        } else {
            return "Insert into Library (library_id, name, reference, seq_type, read_length, " + "read_length_alt, cancer, cancer_type, sample_organ, " + "sample_notes, pubmed_id, file_location, contact, protocol, sample_cell_type, platform_id) VALUES ('" + get_Library_id() + "', '" + get_Name() + "', '" + get_Reference() + "', '" + get_Seq_type() + "', '" + get_Read_length() + "', '" + get_Read_length_alt() + "', '" + get_Cancer() + "', '" + get_cancer_type() + "', '" + get_Sample_organ() + "', '" + get_Sample_notes() + "', '" + get_pubmed_id() + "', '" + get_file_location() + "', '" + get_contact() + "', '" + get_protocol() + "', '" + get_Sample_cell_type() + "', '" + get_Platform_ID() + "')";
        }
    }

    public String update_library_string() {
        String query = "UPDATE library SET name = '" + this.get_Name() + "', reference = '" + this.get_Reference() + "', seq_type = '" + this.get_Seq_type() + "', read_length = '" + this.get_Read_length() + "', read_length_alt = '" + this.get_Read_length_alt() + "', cancer = '" + this.get_Cancer() + "', cancer_type = '" + this.get_cancer_type() + "', sample_organ = '" + this.get_Sample_organ() + "', sample_notes = '" + this.get_Sample_notes() + "', pubmed_id= '" + this.get_pubmed_id() + "', file_location = '" + this.get_file_location() + "', contact = '" + this.get_contact() + "', protocol = '" + this.get_protocol() + "', sample_cell_type= '" + this.get_Sample_cell_type() + "', platform_id = '" + this.get_Platform_ID() + "' WHERE library_id = '" + this.get_Library_id() + "'";
        return query;
    }
}
