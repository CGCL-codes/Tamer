package net.sf.sasl.aop.exception.handling.dummies;

/**
 * Dummy class for an integration test.
 * 
 * @author Philipp Förmer
 * 
 */
public class ServiceDummy {

    public OneDerivedDummyResponseDTO getDerivedDummyResponseDTO() {
        throw new RuntimeException("Something is wrong");
    }

    public DummyResponseDTO getDummyResponseDTO() {
        DummyResponseDTO dto = new DummyResponseDTO();
        dto.setExecuted(true);
        dto.setResultCode("Ok");
        return dto;
    }

    public String notMapped() {
        throw new RuntimeException("Firewall will fail...");
    }
}
