#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.${artifactId}.service.command.order.action;

import ${package}.${artifactId}.service.GeneralResponse;
import ${package}.${artifactId}.service.command.Response;

/**
 * Response returned after executing the DeleteOrderAction in ActionHelloWorldService
 * @author pstepaniak
 *
 */
public class DeleteOrderActionResponse extends GeneralResponse implements Response {
	public DeleteOrderActionResponse() {
		super();
	}
}
