package model;

import utils.NamingUtils;

/**
 * 
 * 네이밍 변환 클래스
 * <p>
 * <b>NOTE:</b> 언더스코어 네이밍을 카멜, 파스칼 케이싱으로 변환하기 위한 유틸리티
 * 
 * @author 개발환경 개발팀 이흥주
 * @since 2009.08.03
 * @version 1.0
 * @see
 * 
 *      <pre>
 *  == 개정이력(Modification Information) ==
 *   
 *   수정일      수정자           수정내용
 *  -------    --------    ---------------------------
 *   2009.08.03  이흥주          최초 생성
 * 
 * </pre>
 */
public class NameCasing {

    /**
	 * 이름
	 */
    private String name;

    /**
	 * UpperCase 이름
	 */
    private String ucName;

    /**
	 * LowerCase 이름
	 */
    private String lcName;

    /**
	 * CamelCase 이름
	 */
    private String ccName;

    /**
	 * PascalCase 이름
	 */
    private String pcName;

    /**
	 * 생성자
	 * 
	 * @param name
	 */
    public NameCasing(String name) {
        setName(name);
    }

    /**
	 * 이름 가져오기
	 * 
	 * @return
	 */
    public String getName() {
        return name;
    }

    /**
	 * 이름 세팅하기
	 * 
	 * @param name
	 */
    public void setName(String name) {
        this.name = name;
        setUcName(name.toUpperCase());
        setLcName(name.toLowerCase());
        setCcName(NamingUtils.convertUnderscoreNameToCamelcase(name));
        setPcName(NamingUtils.convertCamelcaseToPascalcase(getCcName()));
    }

    /**
	 * UpperCase 이름 가져오기
	 * 
	 * @return
	 */
    public String getUcName() {
        return ucName;
    }

    /**
	 * UpperCase 이름 세팅하기
	 * 
	 * @param uppercaseName
	 */
    public void setUcName(String uppercaseName) {
        this.ucName = uppercaseName;
    }

    /**
	 * LowerCase 이름 가져오기
	 * 
	 * @return
	 */
    public String getLcName() {
        return lcName;
    }

    /**
	 * LowerCase 이름 세팅하기
	 * 
	 * @param lowercaseName
	 */
    public void setLcName(String lowercaseName) {
        this.lcName = lowercaseName;
    }

    /**
	 * CamelCase 이름 가져오기
	 * 
	 * @return
	 */
    public String getCcName() {
        return ccName;
    }

    /**
	 * CamelCase 이름 세팅하기
	 * 
	 * @return
	 */
    public void setCcName(String camelcaseName) {
        this.ccName = camelcaseName;
    }

    /**
	 * PascalCase 이름 가져오기
	 * 
	 * @return
	 */
    public String getPcName() {
        return pcName;
    }

    /**
	 * PascalCase 이름 세팅하기
	 * 
	 * @param pascalcaseName
	 */
    public void setPcName(String pascalcaseName) {
        this.pcName = pascalcaseName;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("NameCasing [getName()=");
        builder.append(getName());
        builder.append(", getUcName()=");
        builder.append(getUcName());
        builder.append(", getLcName()=");
        builder.append(getLcName());
        builder.append(", getCcName()=");
        builder.append(getCcName());
        builder.append(", getPcName()=");
        builder.append(getPcName());
        builder.append("]");
        return builder.toString();
    }
}
