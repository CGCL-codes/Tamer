package net.narusas.si.auction.model;

public class 제시외건물 {

    private String 용도;

    private String 구조;

    private String 면적;

    private String 층형;

    private String 종류;

    private 물건 물건;

    private String 포함여부;

    private String 주소;

    private Integer 목록번호;

    Long id;

    public 제시외건물() {
    }

    public 제시외건물(물건 물건, String 용도, String 구조, String 면적, String 층형, String 포함여부, int 목록번호, String 주소) {
        this.물건 = 물건;
        this.용도 = 용도;
        this.구조 = 구조;
        this.면적 = 면적;
        this.층형 = 층형;
        this.포함여부 = 포함여부;
        this.목록번호 = 목록번호;
        this.주소 = 주소;
        종류 = "제시외건물";
    }

    public String get종류() {
        return 종류;
    }

    public void set종류(String 종류) {
        this.종류 = 종류;
    }

    public 물건 get물건() {
        return 물건;
    }

    public void set물건(물건 물건) {
        this.물건 = 물건;
    }

    public String get용도() {
        return 용도;
    }

    public void set용도(String 용도) {
        this.용도 = 용도;
    }

    public String get구조() {
        return 구조;
    }

    public void set구조(String 구조) {
        this.구조 = 구조;
    }

    public String get면적() {
        return 면적;
    }

    public void set면적(String 면적) {
        this.면적 = 면적;
    }

    public String get주소() {
        return 주소;
    }

    public void set주소(String 주소) {
        this.주소 = 주소;
    }

    public Integer get목록번호() {
        return 목록번호;
    }

    public void set목록번호(Integer 목록번호) {
        this.목록번호 = 목록번호;
    }

    public String get층형() {
        return 층형;
    }

    public void set층형(String 층형) {
        this.층형 = 층형;
    }

    public String get포함여부() {
        return 포함여부;
    }

    public void set포함여부(String 포함여부) {
        this.포함여부 = 포함여부;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
