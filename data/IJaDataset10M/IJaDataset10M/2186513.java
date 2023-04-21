package br.com.sysmap.crux.core.client.screen;

/**
 * @author Thiago da Rosa de Bustamante
 *
 */
@Deprecated
public interface RegisteredCruxSerializables {

    CruxSerializable getCruxSerializable(String type);

    void registerCruxSerializable(String type, CruxSerializable serializable);
}
