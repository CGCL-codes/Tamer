package br.com.mcampos.dto.user.login;

import br.com.mcampos.dto.core.SimpleTableDTO;

public class AccessLogTypeDTO extends SimpleTableDTO {

    /**
	 * 
	 */
    private static final long serialVersionUID = -4837247897791432960L;

    public AccessLogTypeDTO(SimpleTableDTO simpleTableDTO) {
        super(simpleTableDTO);
    }

    public AccessLogTypeDTO(Integer integer, String string) {
        super(integer, string);
    }

    public AccessLogTypeDTO(Integer integer) {
        super(integer);
    }

    public AccessLogTypeDTO() {
        super();
    }
}
