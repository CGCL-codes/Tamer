package org.openremote.beehive.rest;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import javax.ws.rs.core.MediaType;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.jboss.resteasy.core.Dispatcher;
import org.jboss.resteasy.mock.MockHttpRequest;
import org.jboss.resteasy.mock.MockHttpResponse;
import org.openremote.beehive.SpringTestContext;
import org.openremote.beehive.TemplateTestBase;
import org.openremote.beehive.api.dto.modeler.DeviceCommandDTO;
import org.openremote.beehive.api.dto.modeler.DeviceDTO;
import org.openremote.beehive.api.dto.modeler.ProtocolAttrDTO;
import org.openremote.beehive.api.dto.modeler.ProtocolDTO;
import org.openremote.beehive.api.service.impl.GenericDAO;
import org.openremote.beehive.domain.modeler.Device;
import org.openremote.beehive.rest.service.DeviceCommandRESTTestService;
import org.openremote.beehive.rest.service.DeviceRESTTestService;
import org.openremote.beehive.utils.FixtureUtil;
import org.openremote.beehive.utils.RESTTestUtils;

/**
 * The Class DeviceCommandRESTServiceTest.
 */
public class DeviceCommandRESTServiceTest extends TemplateTestBase {

    private GenericDAO genericDAO = (GenericDAO) SpringTestContext.getInstance().getBean("genericDAO");

    public void testSaveCommand() throws JsonParseException, JsonMappingException, URISyntaxException, IOException {
        Dispatcher deviceDispatcher = RESTTestUtils.createDispatcher(DeviceRESTTestService.class);
        long deviceId = saveDevice(deviceDispatcher);
        String commandJson = FixtureUtil.getFileContent("dtos/device_command/command.json");
        String postData = commandJson.replaceAll("\"id\":17", "\"id\":" + deviceId);
        Dispatcher dispatcher = RESTTestUtils.createDispatcher(DeviceCommandRESTTestService.class);
        MockHttpRequest mockHttpRequest = MockHttpRequest.post("/devicecommand/save");
        mockHttpRequest.accept(MediaType.APPLICATION_JSON);
        mockHttpRequest.contentType(MediaType.APPLICATION_JSON);
        addCredential(mockHttpRequest);
        mockHttpRequest.content(postData.getBytes());
        MockHttpResponse mockHttpResponse = new MockHttpResponse();
        dispatcher.invoke(mockHttpRequest, mockHttpResponse);
        String dbDeviceCommandJson = mockHttpResponse.getContentAsString();
        ObjectMapper mapper = new ObjectMapper();
        DeviceCommandDTO deviceCommand = mapper.readValue(dbDeviceCommandJson, DeviceCommandDTO.class);
        assertEquals("command1", deviceCommand.getName());
        MockHttpRequest mockLoadHttpRequest = MockHttpRequest.get("/devicecommand/load/" + deviceCommand.getId());
        mockLoadHttpRequest.accept(MediaType.APPLICATION_JSON);
        addCredential(mockLoadHttpRequest);
        MockHttpResponse mockLoadHttpResponse = new MockHttpResponse();
        dispatcher.invoke(mockLoadHttpRequest, mockLoadHttpResponse);
        String dbDeviceCommandJson2 = mockLoadHttpResponse.getContentAsString();
        DeviceCommandDTO deviceCommand2 = mapper.readValue(dbDeviceCommandJson2, DeviceCommandDTO.class);
        assertEquals(deviceCommand.getName(), deviceCommand2.getName());
        deviceCommand2.setName("update command");
        ProtocolDTO protocol = new ProtocolDTO();
        ProtocolAttrDTO protocolAttr = new ProtocolAttrDTO();
        protocolAttr.setName("url");
        protocolAttr.setValue("http://update.command.com");
        List<ProtocolAttrDTO> attributes = new ArrayList<ProtocolAttrDTO>();
        attributes.add(protocolAttr);
        protocol.setAttributes(attributes);
        protocol.setType("HTTP");
        deviceCommand2.setProtocol(protocol);
        MockHttpRequest mockUpdateHttpRequest = MockHttpRequest.post("/devicecommand/update");
        mockUpdateHttpRequest.accept(MediaType.APPLICATION_JSON);
        mockUpdateHttpRequest.contentType(MediaType.APPLICATION_JSON);
        addCredential(mockUpdateHttpRequest);
        String updateCommandJson = mapper.writeValueAsString(deviceCommand2);
        mockUpdateHttpRequest.content(updateCommandJson.getBytes());
        MockHttpResponse mockUpdateHttpResponse = new MockHttpResponse();
        dispatcher.invoke(mockUpdateHttpRequest, mockUpdateHttpResponse);
        deleteDeviceCommand(dispatcher, deviceCommand2.getId());
    }

    public void testSaveAllCommands() throws Exception {
        Dispatcher deviceDispatcher = RESTTestUtils.createDispatcher(DeviceRESTTestService.class);
        long deviceId = saveDevice(deviceDispatcher);
        String commandsJson = FixtureUtil.getFileContent("dtos/device_command/commands.json");
        String postData = commandsJson.replaceAll("\"id\":20", "\"id\":" + deviceId);
        Dispatcher dispatcher = RESTTestUtils.createDispatcher(DeviceCommandRESTTestService.class);
        MockHttpRequest mockHttpRequest = MockHttpRequest.post("/devicecommand/saveall");
        mockHttpRequest.accept(MediaType.APPLICATION_JSON);
        mockHttpRequest.contentType(MediaType.APPLICATION_JSON);
        addCredential(mockHttpRequest);
        mockHttpRequest.content(postData.getBytes());
        MockHttpResponse mockHttpResponse = new MockHttpResponse();
        dispatcher.invoke(mockHttpRequest, mockHttpResponse);
        assertEquals(200, mockHttpResponse.getStatus());
    }

    private long saveDevice(Dispatcher deviceDispatcher) throws URISyntaxException, JsonParseException, JsonMappingException, IOException {
        MockHttpRequest mockHttpRequest = MockHttpRequest.post("/device/save/1");
        mockHttpRequest.accept(MediaType.APPLICATION_JSON);
        mockHttpRequest.contentType(MediaType.APPLICATION_JSON);
        addCredential(mockHttpRequest);
        String postData = FixtureUtil.getFileContent("dtos/device/device_simple.json");
        mockHttpRequest.content(postData.getBytes());
        MockHttpResponse mockHttpResponse = new MockHttpResponse();
        deviceDispatcher.invoke(mockHttpRequest, mockHttpResponse);
        String dbDeviceJson = mockHttpResponse.getContentAsString();
        ObjectMapper mapper = new ObjectMapper();
        DeviceDTO dbDevice = mapper.readValue(dbDeviceJson, DeviceDTO.class);
        return dbDevice.getId();
    }

    private void deleteDeviceCommand(Dispatcher dispatcher, long deviceCommandId) throws URISyntaxException {
        MockHttpRequest mockHttpRequest = MockHttpRequest.delete("/devicecommand/delete/" + deviceCommandId);
        addCredential(mockHttpRequest);
        MockHttpResponse mockHttpResponse = new MockHttpResponse();
        dispatcher.invoke(mockHttpRequest, mockHttpResponse);
        assertEquals(200, mockHttpResponse.getStatus());
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
        List<Device> devices = genericDAO.loadAll(Device.class);
        genericDAO.deleteAll(devices);
    }
}
