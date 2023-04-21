package net.sf.balm.common.dto;

/**
 * 
 * 
 * @author dz
 */
public class ReferenceDto extends SimpleDto {

    public ReferenceDto(Object member) {
        super(member);
    }

    public void accept(SimpleDtoVisitor visitor) {
    }
}
